package com.ue.ps.systems;

import java.util.ArrayList;

import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;

import com.ue.ps.Planet;
import com.ue.ps.buildings.Building;
import com.ue.ps.buildings.Building.Types;
import com.ue.ps.buildings.Factory;
import com.ue.ps.buildings.Mine;
import com.ue.ps.buildings.SpaceFactory;

/**
 * PlanetNetwork objects store planets in groups based on if they are connected by "Lines". These networks are intended to be used to ease the process
 * of sharing resources between linked planets.
 * 
 * @author zscha
 *
 */
public class PlanetNetwork implements Destroyable {

	private static ArrayList<PlanetNetwork> activeNetworks = new ArrayList<PlanetNetwork>();

	private int sumRes, sumResGain, sumResUse;

	private ArrayList<Planet> linkedPlanets;
	private Planet netSeed;

	/**
	 * Generates a network with seed planet p.
	 * 
	 * @param p The "seed" of the new network
	 * 
	 * @author zschaefle
	 */
	public PlanetNetwork(Planet p) {
		this.netSeed = p;
		this.linkedPlanets = new ArrayList<Planet>();
		this.linkedPlanets.add(netSeed);
		this.updateNetwork();

		activeNetworks.add(this);
	}

	/**
	 * Generates a PlanetNetwork from a scrap array, and confirms it's validity. If for whatever reason one or more of the planets it is given cannot
	 * join the network, the process is repeated with the remaining planets.
	 * 
	 * @param p The "seed" of the new network
	 * @param in The other planets that need a network
	 * 
	 * @author zschaefle
	 */
	private PlanetNetwork(Planet p, ArrayList<Planet> in) {
		this.netSeed = p;
		this.linkedPlanets = in;
		activeNetworks.add(this);
		this.updateNetwork();
	}

	/**
	 * Starting with the seed planet, indexes interconnected planets. Once each planet is scanned for connected "neighbors", those neighbors are
	 * scanned for their connections, ignoring already found planets. Planets that were previously not included in the network are added to it.
	 * 
	 * @author zschaefle
	 */
	public void updateNetwork() {
		ArrayList<Integer> proven = new ArrayList<Integer>(), checked = new ArrayList<Integer>();
		for (int i = 0; i < linkedPlanets.size(); i++) {
			if (linkedPlanets.get(i).equals(netSeed)) {
				proven.add(i);
				break;
			}
		}

		loop: while (true) {
			for (int i : proven) { // All indexes of planets in the linkedPlanets array previously confirmed
				if (!checked.contains(i)) { // No double checking
					for (Planet p : this.linkedPlanets.get(i).lineLinkedPlanets) { // All planets known to be connected to the planet with ^ index
						int temp = this.linkedPlanets.indexOf(p); // Grab the index of the planet to be checked if it has one already
						if (temp == -1) {
							linkedPlanets.add(p); // Add planets that were just noticed to be
													// connected
							proven.add(linkedPlanets.indexOf(p));
						} else if (temp >= 0 && !proven.contains(temp)) {
							proven.add(temp); // "Prove" the planet exists
						}
					}
					checked.add(i); // Register as checked
				}
			}

			if (checked.containsAll(proven)) {
				break loop;
			}
		}

		ArrayList<Integer> scrap = new ArrayList<Integer>();
		for (int i = 0; i < linkedPlanets.size(); i++) {
			if (!proven.contains(i)) {
				scrap.add(0, i);
			}
		}

		ArrayList<Planet> newSet = new ArrayList<Planet>();
		for (int i : scrap) {
			newSet.add(linkedPlanets.get(i));
			linkedPlanets.remove(i);
		}

		new PlanetNetwork(newSet.get(0), newSet);

		boolean flag = false;
		for (Planet p : this.linkedPlanets) {
			if (!p.linkNetwork.equals(this)) {
				p.linkNetwork = this;
				flag = true;
			}
		}
		if (flag) {
			PlanetNetwork.checkAllNetworks();
		}

		this.testTerminate();

	}

	/**
	 * Joins one or more PlanetNetworks to the invoking PlanetNetwork. The seedWorld will remain the same for the invoking PlanetNetwork.
	 * PlanetNetworks passed in will be have their planets copied, then be destroyed. The consolidated PlanetNetwork will then be updated.
	 * 
	 * @param nets
	 */
	public void mergeWith(PlanetNetwork ... nets) {
		for (PlanetNetwork n : nets) {
			for (Planet p : n.linkedPlanets) {
				this.linkedPlanets.add(p);
				n.linkedPlanets.remove(p);
			}
			this.updateNetwork();
			n.testTerminate();
		}
	}

	/**
	 * Destroys the network if there is one (or less) planets contained in it
	 */
	public void testTerminate() {
		if (this.linkedPlanets.size() <= 1) {
			try {
				activeNetworks.remove(this);
				this.destroy();
			} catch (DestroyFailedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return A list of all active PlanetNetwork objects
	 */
	public static ArrayList<PlanetNetwork> getActiveNetworks() {
		return activeNetworks;
	}

	public static void checkAllNetworks() {
		boolean flag = false;
		ArrayList<Planet> networkedPlanets = new ArrayList<Planet>();
		ArrayList<PlanetNetwork[]> dupes = new ArrayList<PlanetNetwork[]>();
		for (PlanetNetwork pn : activeNetworks) {
			// TODO: check all networks for overlap and merge overlapping networks.
			for (Planet p : pn.linkedPlanets) {
				if (!p.linkNetwork.equals(pn)) {
					flag = true;
					PlanetNetwork[] dupe = { pn, p.linkNetwork };
					dupes.add(dupe);
				}
			}
		}
		for (PlanetNetwork[] d : dupes) {
			d[0].mergeWith(d[1]);
		}
		if (flag) {
			checkAllNetworks();
		}
	}

	public void updateStats() {
		for (Planet p : linkedPlanets) {
			sumRes += p.resource;
			for (Building b : p.landBuildings) {
				if (b.type == Types.factory) {
					sumResUse += ((Factory) b).getConsumption(p);
				} else if (b.type == Types.mine) {
					sumResGain += ((Mine) b).production;
				}
			}
			for (Building b : p.spaceBuildings) {
				if (b.type == Types.spaceFactory) {
					sumResUse += ((SpaceFactory) b).getConsumption(p);
				}
			}
		}
	}

	public ArrayList<Planet> getAbundantPlanets() {
		ArrayList<Planet> out = new ArrayList<Planet>();
		for (Planet p : linkedPlanets) {

			if (p.getExcess() > 0) {
				out.add(p);
			}
		}
		return out;
	}

	public void update() {
		ArrayList<Planet> excess = getAbundantPlanets();

		int remaining = 0;
		for (Planet p : linkedPlanets) {
			if (p.getExcess() <= 0) {
				remaining += 1;
			}
		}

		while (!(excess.isEmpty() || remaining <= 0)) {
			for (Planet p : excess) {
				for (Planet pl : p.lineLinkedPlanets) {
					if (p.getExcess() > 0 && pl.getExcess() < 0) {
						pl.resource += 1;
						p.resource -= 1;
					}
				}
				for (Planet pl : p.lineLinkedPlanets) {
					if (p.getExcess() > 0 && pl.getExcess() == 0) {
						pl.resource += 1;
						p.resource -= 1;
					}
				}
			}

			excess = getAbundantPlanets();

			remaining = 0;
			for (Planet p : linkedPlanets) {
				if (p.getExcess() <= 0) {
					remaining += 1;
				}
			}

		}

	}
}
