package br.ufpb.ia.ag.entity;

import java.util.ArrayList;
import java.util.List;

import br.ufpb.ia.ag.exception.HorarioExistenteException;
import br.ufpb.ia.ag.exception.HorarioInexistenteException;

public class Professor {
	
	private String matricula;
	private String nome;
	private String senha;
	private String email;
	private List<Horario> horariosPreferidos;
	
	public Professor(String matricula, String nome, String senha, String email) {
		this.matricula = matricula;
		this.nome = nome;
		this.senha = senha;
		this.email = email;
		this.horariosPreferidos = new ArrayList<Horario>();
	}
	
	public Professor() {
		this.horariosPreferidos = new ArrayList<Horario>();
	}
	
	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Horario> getHorariosPreferidos() {
		return horariosPreferidos;
	}

	public void setHorariosPreferidos(List<Horario> horariosPreferidos) {
		this.horariosPreferidos = horariosPreferidos;
	}

	public void cadastrarHorarioPreferido(Horario horario) throws HorarioExistenteException {
		if(existsHorario(horario)) {
			throw new HorarioExistenteException("O horário de preferência já existe.");
		} 
		this.horariosPreferidos.add(horario);
	}

	public void removerHorarioPreferido(String idHorarioPreferido) throws HorarioInexistenteException {
		boolean removeu = false;
		for(Horario h: this.horariosPreferidos) {
			if(h.equals(idHorarioPreferido)) {
				this.horariosPreferidos.remove(h);
				removeu = true;
			}
		}
		if(!removeu) {
			throw new HorarioInexistenteException("O horário de preferência não existe.");
		}
		
	}
	
	public boolean existsHorario(Horario horario) {
		// Atualizado com o novo design de um objeto Horario
		for(Horario h: horariosPreferidos) {
			if(h.getDiaDaSemana().equals(horario.getDiaDaSemana()) && h.getHorarioAula().equals(horario.getHorarioAula())) {
				return true;
			}
		}
		return false;
	}

}
