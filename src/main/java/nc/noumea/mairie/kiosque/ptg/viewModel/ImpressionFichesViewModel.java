package nc.noumea.mairie.kiosque.ptg.viewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.kiosque.ptg.dto.FichePointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.JourPointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.PrimeDto;
import nc.noumea.mairie.ws.ISirhPtgWSConsumer;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Checkbox;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ImpressionFichesViewModel {

	@WireVariable
	private ISirhPtgWSConsumer ptgWsConsumer;

	private FichePointageDto ficheCourante;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	private JourPointageDto premierJour;

	@Init
	public void initSaisieFichePointage() throws ParseException {
		
		ProfilAgentDto currentUser = (ProfilAgentDto) Sessions.getCurrent().getAttribute("currentUser");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		FichePointageDto result = ptgWsConsumer.getFichePointageSaisie(currentUser.getAgent().getIdAgent(), sdf.parse("08/09/2014"), currentUser.getAgent().getIdAgent());
		setFicheCourante(result);
		setPremierJour(getFicheCourante().getSaisies().get(0));
	}

	@Command
	@NotifyChange({ "ficheCourante", "premierJour" })
	public void checkAccorde(@BindingParam("prime") PrimeDto prime, @BindingParam("checkbox") Checkbox checkbox) {
		prime.setQuantite(checkbox.isChecked() ? 1 : null);
	}

	public FichePointageDto getFicheCourante() {
		return ficheCourante;
	}

	public void setFicheCourante(FichePointageDto ficheCourante) {
		this.ficheCourante = ficheCourante;
	}

	public String getLundi() {
		return sdf.format(getFicheCourante().getDateLundi());
	}

	public String getMardi() {
		return getDatePlusJour(getFicheCourante().getDateLundi(), 1);
	}

	public String getMercredi() {
		return getDatePlusJour(getFicheCourante().getDateLundi(), 2);
	}

	public String getJeudi() {
		return getDatePlusJour(getFicheCourante().getDateLundi(), 3);
	}

	public String getVendredi() {
		return getDatePlusJour(getFicheCourante().getDateLundi(), 4);
	}

	public String getSamedi() {
		return getDatePlusJour(getFicheCourante().getDateLundi(), 5);
	}

	public String getDimanche() {
		return getDatePlusJour(getFicheCourante().getDateLundi(), 6);
	}

	private String getDatePlusJour(Date dateChoisi, int nbJours) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateChoisi);
		cal.add(Calendar.DAY_OF_MONTH, nbJours);
		return sdf.format(cal.getTime());
	}

	public JourPointageDto getPremierJour() {
		return premierJour;
	}

	public void setPremierJour(JourPointageDto premierJour) {
		this.premierJour = premierJour;
	}
}
