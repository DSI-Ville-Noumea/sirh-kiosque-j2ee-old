package nc.noumea.mairie.kiosque.abs.planning;

/*
 * #%L
 * sirh-kiosque-j2ee
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 - 2015 Mairie de Nouméa
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import nc.noumea.mairie.kiosque.abs.dto.DemandeDto;
import nc.noumea.mairie.kiosque.abs.planning.vo.CustomColorAgent;
import nc.noumea.mairie.kiosque.abs.planning.vo.CustomDHXCollection;
import nc.noumea.mairie.kiosque.abs.planning.vo.CustomDHXEvent;
import nc.noumea.mairie.kiosque.dto.AgentDto;

import com.dhtmlx.planner.DHXEv;
import com.dhtmlx.planner.DHXEvent;
import com.dhtmlx.planner.DHXEventsManager;
import com.dhtmlx.planner.DHXStatus;
import com.dhtmlx.planner.data.DHXCollection;
 
public class CustomEventsManager extends DHXEventsManager {

	public static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	
	public static final List<String> LIST_COLORS = Arrays.asList("orange", "red", "gold", "blue", "green", "grey", "purple", "pink"); 

	private List<DHXEv> evs = new ArrayList<DHXEv>();
	
	List<CustomColorAgent> listColorAgent = new ArrayList<CustomColorAgent>();
	
	ArrayList<DHXCollection> timeLine;
	
 
	public CustomEventsManager(HttpServletRequest request) {
		super(request);
	}
	
	public CustomEventsManager(HttpServletRequest request, List<DemandeDto> listDemandesDto, List<AgentDto> listAgents) {
		super(request);
		
		if(null != listDemandesDto) {
			for(DemandeDto dto : listDemandesDto) {
				CustomDHXEvent ev = new CustomDHXEvent(dto);
				ev.setDateDebut(sdf.format(dto.getDateDebut()));
				ev.setDateFin(sdf.format(dto.getDateFin()));
				getEvs().add(ev);
			}
		}
		
		setTimeLine(createTimeLine(listAgents));
	}
	
	private ArrayList<DHXCollection> createTimeLine(List<AgentDto> listAgents) {
		
		Collections.sort(listAgents);
		
		ArrayList<DHXCollection> event_type = new ArrayList<DHXCollection>();
		boolean className = false; 
		for(AgentDto agent : listAgents) {
			event_type.add(new CustomDHXCollection(agent.getIdAgent().toString(), agent.getNom() + " " + agent.getPrenom(), className));
			className = !className;
		}
		
		return event_type;
	}
 
	public Iterable<DHXEv> getEvents() {
    	return getEvs();
	}
 
	@Override
	public DHXStatus saveEvent(DHXEv event, DHXStatus status) {
		return status;
	}
 
	@Override
	public DHXEv createEvent(String id, DHXStatus status) {
		return new DHXEvent();
	}
	
	@Override
	public HashMap<String, Iterable<DHXCollection>> getCollections() {

		HashMap<String, Iterable<DHXCollection>> c = new HashMap<String, Iterable<DHXCollection>>();
		c.put("viewSemaine", getTimeLine());
		c.put("viewMois", getTimeLine());
		c.put("viewTrimestre", getTimeLine());
		return c;
	}

	private String getColor(Integer idAgent) {
		
		// si l agent a deja etait traite, on retourne la meme couleur
		for(CustomColorAgent colorAgent : listColorAgent) {
			if(colorAgent.getIdAgent().equals(idAgent)) {
				return colorAgent.getColor();
			}
		}
		
		// si 1er fois qu on traite l agent
		// on lui attribue une couleur
		for(String color : LIST_COLORS) {
			boolean isColorFind = false;
			for(CustomColorAgent colorAgent : listColorAgent) {
				if(colorAgent.getColor().equals(color)) {
					isColorFind = true;
					break;
				}
			}
			if(!isColorFind) {
				CustomColorAgent colorAgent = new CustomColorAgent(idAgent, color);
				listColorAgent.add(colorAgent);
				return color;
			}
		}
		
		return "black";
	}

	
	public ArrayList<DHXCollection> getTimeLine() {
		return timeLine;
	}

	public void setTimeLine(ArrayList<DHXCollection> timeLine) {
		this.timeLine = timeLine;
	}

	public List<DHXEv> getEvs() {
		return evs;
	}

	public void setEvs(List<DHXEv> evs) {
		this.evs = evs;
	}
	
	
}