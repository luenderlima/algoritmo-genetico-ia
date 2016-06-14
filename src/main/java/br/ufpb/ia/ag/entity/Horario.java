package br.ufpb.ia.ag.entity;

public class Horario {
	
	private String horarioAula;
	private String diaDaSemana;
	
	public Horario(String horarioAula, String diaDaSemana) {
		this.horarioAula = horarioAula;
		this.diaDaSemana = diaDaSemana;
	}

	public String getHorarioAula() {
		return horarioAula;
	}
	
	public void setHorarioAula(String horarioAula) {
		this.horarioAula = horarioAula;
	}
	
	public String getDiaDaSemana() {
		return diaDaSemana;
	}
	
	public void setDiaDaSemana(String diaDaSemana) {
		this.diaDaSemana = diaDaSemana;
	}

}
