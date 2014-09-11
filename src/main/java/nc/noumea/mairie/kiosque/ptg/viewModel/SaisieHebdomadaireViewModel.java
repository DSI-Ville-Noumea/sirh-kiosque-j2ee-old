package nc.noumea.mairie.kiosque.ptg.viewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import nc.noumea.mairie.kiosque.ptg.dto.FichePointageDto;
import nc.noumea.mairie.kiosque.ptg.dto.JourPointageDto;
import nc.noumea.mairie.ws.ISirhPtgWSConsumer;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class SaisieHebdomadaireViewModel {

	@WireVariable
	private ISirhPtgWSConsumer ptgWsConsumer;

	private FichePointageDto ficheCourante;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	private JourPointageDto premierJour;

	@Init
	public void initSaisieFichePointage() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		FichePointageDto result = ptgWsConsumer.getFichePointageSaisie(9005138, sdf.parse("08/09/2014"), 9005138);
		setFicheCourante(result);
		setPremierJour(getFicheCourante().getSaisies().get(0));
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
