package nc.noumea.mairie.kiosque.travail.viewModel;

/*
 * #%L
 * sirh-kiosque-j2ee
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 Mairie de Nouméa
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

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.noumea.mairie.kiosque.cmis.ISharepointService;
import nc.noumea.mairie.kiosque.cmis.SharepointDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class EaeViewModel {

	private List<SharepointDto> listeUrlEae;

	private ProfilAgentDto currentUser;

	@WireVariable
	private ISharepointService sharepointConsumer;

	@Init
	public void iniEaeAgent() throws Exception {

		currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		List<SharepointDto> res = sharepointConsumer.getAllEae(currentUser.getAgent().getIdAgent());

		// on tri la liste par année
		Collections.sort(res, new Comparator<SharepointDto>() {
			@Override
			public int compare(SharepointDto o1, SharepointDto o2) {
				if (null == o2.getAnnee()) {
					return 0;
				}
				return o2.getAnnee().compareTo(o1.getAnnee());
			}

		});

		// on ne garde que les 3 dernieres EAEs
		if (res != null && res.size() > 3) {
			setListeUrlEae(res.subList(0, 3));
		} else {
			setListeUrlEae(res);
		}
	}

	@Command
	public void visuEAE(@BindingParam("ref") String url) {
		// create a window programmatically and use it as a modal dialog.
		Map<String, String> args = new HashMap<String, String>();
		args.put("url", url);
		Window win = (Window) Executions.createComponents("/travail/visuEae.zul", null, args);
		win.doModal();
	}

	public List<SharepointDto> getListeUrlEae() {
		return listeUrlEae;
	}

	public void setListeUrlEae(List<SharepointDto> listeUrlEae) {
		this.listeUrlEae = listeUrlEae;
	}
}
