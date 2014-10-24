package nc.noumea.mairie.kiosque.validation;

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


import java.util.List;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

public class Validation {
	private List<ValidationMessage> errorsMessages = null;
	private List<ValidationMessage> infosMessages = null;

	@Init
	public void init(@ExecutionArgParam("errors") List<ValidationMessage> errors,
			@ExecutionArgParam("infos") List<ValidationMessage> infos) {
		setErrorsMessages(errors);
		setInfosMessages(infos);
	}

	public List<ValidationMessage> getErrorsMessages() {
		return errorsMessages;
	}

	public void setErrorsMessages(List<ValidationMessage> errorsMessages) {
		this.errorsMessages = errorsMessages;
	}

	public List<ValidationMessage> getInfosMessages() {
		return infosMessages;
	}

	public void setInfosMessages(List<ValidationMessage> infosMessages) {
		this.infosMessages = infosMessages;
	}
}
