package nc.noumea.mairie.kiosque.eae.dto;

public class EaeEvaluationDto {

	private int idEae;
	private int anneeAvancement;
	private boolean avisChangementClasse;
	private boolean avisRevalorisation;
	private boolean cap;
	private String commentaireAvctEvaluateur;
	private String commentaireAvctEvalue;
	private String commentaireEvaluateur;
	private String commentaireEvalue;
	private DureeDto dureeEntretien;
	private EaeListeDto niveau;
	private Double noteAnnee;
	private Double noteAnneeN1;
	private Double noteAnneeN2;
	private Double noteAnneeN3;
	private EaeListeDto propositionAvancement;
	private String statut;
	private String typeAvct;

	public int getIdEae() {
		return idEae;
	}

	public void setIdEae(int idEae) {
		this.idEae = idEae;
	}

	public int getAnneeAvancement() {
		return anneeAvancement;
	}

	public void setAnneeAvancement(int anneeAvancement) {
		this.anneeAvancement = anneeAvancement;
	}

	public boolean isAvisChangementClasse() {
		return avisChangementClasse;
	}

	public void setAvisChangementClasse(boolean avisChangementClasse) {
		this.avisChangementClasse = avisChangementClasse;
	}

	public boolean isAvisRevalorisation() {
		return avisRevalorisation;
	}

	public void setAvisRevalorisation(boolean avisRevalorisation) {
		this.avisRevalorisation = avisRevalorisation;
	}

	public boolean isCap() {
		return cap;
	}

	public void setCap(boolean cap) {
		this.cap = cap;
	}

	public String getCommentaireAvctEvaluateur() {
		return commentaireAvctEvaluateur;
	}

	public void setCommentaireAvctEvaluateur(String commentaireAvctEvaluateur) {
		this.commentaireAvctEvaluateur = commentaireAvctEvaluateur;
	}

	public String getCommentaireAvctEvalue() {
		return commentaireAvctEvalue;
	}

	public void setCommentaireAvctEvalue(String commentaireAvctEvalue) {
		this.commentaireAvctEvalue = commentaireAvctEvalue;
	}

	public String getCommentaireEvaluateur() {
		return commentaireEvaluateur;
	}

	public void setCommentaireEvaluateur(String commentaireEvaluateur) {
		this.commentaireEvaluateur = commentaireEvaluateur;
	}

	public String getCommentaireEvalue() {
		return commentaireEvalue;
	}

	public void setCommentaireEvalue(String commentaireEvalue) {
		this.commentaireEvalue = commentaireEvalue;
	}

	public DureeDto getDureeEntretien() {
		return dureeEntretien;
	}

	public void setDureeEntretien(DureeDto dureeEntretien) {
		this.dureeEntretien = dureeEntretien;
	}

	public EaeListeDto getNiveau() {
		return niveau;
	}

	public void setNiveau(EaeListeDto niveau) {
		this.niveau = niveau;
	}

	public EaeListeDto getPropositionAvancement() {
		return propositionAvancement;
	}

	public void setPropositionAvancement(EaeListeDto propositionAvancement) {
		this.propositionAvancement = propositionAvancement;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public String getTypeAvct() {
		return typeAvct;
	}

	public void setTypeAvct(String typeAvct) {
		this.typeAvct = typeAvct;
	}

	public Double getNoteAnnee() {
		return noteAnnee;
	}

	public void setNoteAnnee(Double noteAnnee) {
		this.noteAnnee = noteAnnee;
	}

	public Double getNoteAnneeN1() {
		return noteAnneeN1;
	}

	public void setNoteAnneeN1(Double noteAnneeN1) {
		this.noteAnneeN1 = noteAnneeN1;
	}

	public Double getNoteAnneeN2() {
		return noteAnneeN2;
	}

	public void setNoteAnneeN2(Double noteAnneeN2) {
		this.noteAnneeN2 = noteAnneeN2;
	}

	public Double getNoteAnneeN3() {
		return noteAnneeN3;
	}

	public void setNoteAnneeN3(Double noteAnneeN3) {
		this.noteAnneeN3 = noteAnneeN3;
	}
}
