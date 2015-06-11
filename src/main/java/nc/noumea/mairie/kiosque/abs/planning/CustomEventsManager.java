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
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import nc.noumea.mairie.kiosque.abs.dto.DemandeDto;
import nc.noumea.mairie.kiosque.abs.planning.vo.CustomDHXEvent;
import nc.noumea.mairie.kiosque.dto.AgentWithServiceDto;

import com.dhtmlx.planner.DHXEv;
import com.dhtmlx.planner.DHXEvent;
import com.dhtmlx.planner.DHXEventsManager;
import com.dhtmlx.planner.DHXStatus;
 
public class CustomEventsManager extends DHXEventsManager {

	public static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	
	public static final List<String> LIST_COLORS = Arrays.asList("orange", "red", "gold", "blue", "green", "grey", "purple", "pink"); 

	private List<DHXEv> evs = new ArrayList<DHXEv>();
	
//	ArrayList<DHXCollection> timeLine;
	
 
	public CustomEventsManager(HttpServletRequest request) {
		super(request);
	}
	
	public CustomEventsManager(HttpServletRequest request, List<DemandeDto> listDemandesDto, List<AgentWithServiceDto> listAgents) {
		super(request);
		
		if(null != listDemandesDto) {
			for(DemandeDto dto : listDemandesDto) {
				if(null != dto.getGroupeAbsence()) {
					CustomDHXEvent ev = new CustomDHXEvent(dto);
					ev.setDateDebut(sdf.format(dto.getDateDebut()));
					ev.setDateFin(sdf.format(dto.getDateFin()));
					
					getEvs().add(ev);
				}
			}
		}
	    		
//		setTimeLine(createTimeLine(listAgents));
	}
	
//	private ArrayList<DHXCollection> createTimeLine(List<AgentWithServiceDto> listAgents) {
//		
//		// on trie les agents par ordre alpha
//		Collections.sort(listAgents);
//		ArrayList<DHXCollection> event_type = new ArrayList<DHXCollection>();
//		
//		List<ServiceDto> listeServices = getListServices(listAgents);
//		// on trie les services par ordre alpha
//		Collections.sort(listeServices);
//		
//		CustomDHXCollection test = new CustomDHXCollection("9002990", "test", false);
//		test.addChild(new CustomDHXCollection("9002990", "testjo", false));
//		event_type.add(test);
//		for(ServiceDto service : listeServices) {
//			CustomDHXCollection parent = new CustomDHXCollection(service.getCodeService(), service.getService(), false);
//			
//			boolean className = false; 
//			for(AgentWithServiceDto agent : listAgents) {
//				if(service.getCodeService().equals(agent.getCodeService())) {
//					parent.addChild(new CustomDHXCollection(agent.getIdAgent().toString(), agent.getNom() + " " + agent.getPrenom(), className));
//					className = !className;
//				}
//			}
//			event_type.add(parent);
//		}
//		
//		boolean className = false; 
//		for(AgentWithServiceDto agent : listAgents) {
//			if(service.getCodeService().equals(agent.getCodeService())) {
//				event_type.add(new CustomDHXCollection(agent.getIdAgent().toString(), agent.getNom() + " " + agent.getPrenom(), className));
//				className = !className;
//			}
//		}
//		
//		return event_type;
//	}
	
//	private List<ServiceDto> getListServices(List<AgentWithServiceDto> listAgents) {
//		
//		List<ServiceDto> listServices = new ArrayList<ServiceDto>();
//		for(AgentWithServiceDto agent : listAgents) {
//			ServiceDto service = new ServiceDto();
//			service.setCodeService(agent.getCodeService());
//			service.setService(agent.getService());
//			if(!listServices.contains(service)) {
//				listServices.add(service);
//			}
//		}
//		return listServices;
//	}
 
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
	
//	@Override
//	public HashMap<String, Iterable<DHXCollection>> getCollections() {
//
//		HashMap<String, Iterable<DHXCollection>> c = new HashMap<String, Iterable<DHXCollection>>();
//		c.put("viewSemaine", getTimeLine());
//		c.put("viewMois", getTimeLine());
//		c.put("viewTrimestre", getTimeLine());
//		
//		return c;
//	}
	
//	public ArrayList<DHXCollection> getTimeLine() {
//		return timeLine;
//	}
//
//	public void setTimeLine(ArrayList<DHXCollection> timeLine) {
//		this.timeLine = timeLine;
//	}

	public List<DHXEv> getEvs() {
		return evs;
	}

	public void setEvs(List<DHXEv> evs) {
		this.evs = evs;
	}
	
	
}