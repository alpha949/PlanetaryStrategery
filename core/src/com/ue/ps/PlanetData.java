package com.ue.ps;

import com.ue.ps.systems.GameServerClient;

public class PlanetData {
	public PlanetType type;
	public int size;
	public int x;
	public int y;
	public boolean isHomePlanet;
	public int id;
	public String owner;

	public PlanetData() {

	}

	public static Planet fromPlanetData(PlanetData pd) {
		Planet p = new Planet();
		p.setCenter(pd.x, pd.y);
		p.isHomePlanet = pd.isHomePlanet;
		p.setType(pd.type);
		p.setSize(pd.size);
		p.id = pd.id;
		p.owner = GameServerClient.getPlayerByUserName(pd.owner);

		return p;
	}

	public static PlanetData toPlanetData(Planet p) {
		PlanetData pd = new PlanetData();
		pd.x = (int) p.center.x;
		pd.y = (int) p.center.y;
		pd.isHomePlanet = p.isHomePlanet;
		pd.type = p.getPlanetType();
		pd.size = p.getSize();
		pd.id = p.id;
		if (p.owner != null) {
			pd.owner = p.owner.getUser();

		}

		return pd;
	}

}
