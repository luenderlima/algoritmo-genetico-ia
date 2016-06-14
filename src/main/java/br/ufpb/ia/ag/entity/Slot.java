package br.ufpb.ia.ag.entity;

public class Slot {

	private Disciplina disciplina;
	private Horario horario;
	private boolean apto;
	
	public Slot(Disciplina disciplina, Horario horario) {
		this.disciplina = disciplina;
		this.horario = horario;
	}
	
	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public Horario getHorario() {
		return horario;
	}

	public void setHorario(Horario horario) {
		this.horario = horario;
	}

	public boolean isApto() {
		return apto;
	}

	public void setApto(boolean apto) {
		this.apto = apto;
	}
	
}
