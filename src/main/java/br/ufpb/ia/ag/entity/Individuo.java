package br.ufpb.ia.ag.entity;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.ufpb.ia.ag.algorithm.Algoritmo;

public class Individuo implements Comparable<Individuo> {

	//Os genes do indiv�duo � uma lista bidimensional, que representa uma grade hor�ria composta por slots 	
	private List<List<Slot>> grade;
	private int aptidao;   

    /**
     * Gera um indiv�duo aleat�rio
     */
    public Individuo(int numGenes) {  
    	this.grade = new ArrayList<List<Slot>>();
    	Random random = new Random();
    	List<Disciplina> disciplinasNaoAlocadas = new ArrayList<Disciplina>();
    	    	
    	//Adiciona as disciplinas que deverão ser alocadas na lista auxiliar
    	for(Disciplina disciplina: Algoritmo.getDisciplinas()) {
    		disciplinasNaoAlocadas.add(disciplina);
    	}
    	
    	//Inicializa as listas que representam os dias da semana
    	for(int i = 0; i < Dados.getDiasDaSemana().length; i++) {
    		this.grade.add(new ArrayList<Slot>());
    	} 
    	
    	//Aloca todas as disciplinas de modo aleatório na grade horária
    	for (int i = 0; i < Algoritmo.getDisciplinas().size(); i++) {
    		Horario horario = geraHorario();
    		
	        //Adiciona o slot contendo a disciplina e o respectivo horário em que será ministrada
	        this.grade.get(Dados.getIndiceDiaDaSemana(horario.getDiaDaSemana())).add(
	        								new Slot(disciplinasNaoAlocadas.remove(random.nextInt(disciplinasNaoAlocadas.size())), horario));
	        
        }
    	
        geraAptidao(); 
    	 
    }

	/**
     *  Cria um indiv�duo com os genes definidos
     */
    public Individuo(List<List<Slot>> genes) {   
    	Random random = new Random();
        this.grade = genes;
    
        //Se for mutar, inverte a posi��o entre dois genes
        if (random.nextDouble() <= Algoritmo.getTaxaDeMutacao()) {
        	
        	int indiceDia1 = random.nextInt(Dados.getDiasDaSemana().length);
        	//Loop enquanto o dia sorteado n�o possuir nenhum slot alocado
        	while(genes.get(indiceDia1).size() == 0){
        		indiceDia1 = random.nextInt(Dados.getDiasDaSemana().length);
        	}
        	int indiceDia2 = random.nextInt(Dados.getDiasDaSemana().length);
        	//Loop enquanto o dia sorteado n�o possuir nenhum slot alocado
        	while(genes.get(indiceDia2).size() == 0){
        		indiceDia2 = random.nextInt(Dados.getDiasDaSemana().length);
        	}
        	
        	int posicaoAleatoria1 = random.nextInt(genes.get(indiceDia1).size());
        	int posicaoAleatoria2 = random.nextInt(genes.get(indiceDia2).size());
        	
        	Slot slot1 = genes.get(indiceDia1).get(posicaoAleatoria1);
        	Slot slot2 = genes.get(indiceDia2).get(posicaoAleatoria2);
        	
        	//Substitui a posi��o de dois genes aleat�rios
        	genes.get(indiceDia1).set(posicaoAleatoria1, slot2);
        	genes.get(indiceDia2).set(posicaoAleatoria2, slot1);
        	
        }
        this.grade = genes;
        geraAptidao();
    }

    /**
     * Calcula o valor de aptid�o do indiv�duo
     */
    private void geraAptidao() {
    	this.aptidao = 0;
    	boolean acertou;
    	
    	for(List<Slot> slots: this.grade) {
    		for(Slot slot: slots) {
    			acertou = false;
    			
    			//Varre a lista de hor�rios preferidos do professor
    			for(Horario horarioDisponivel: slot.getDisciplina().getProfessor().getHorariosPreferidos()) {
    				
    				// Verifica se o hor�rio do slot � um hor�rio de prefer�ncia do professor
    				if(horarioDisponivel.getDiaDaSemana().equals(slot.getHorario().getDiaDaSemana())
    					&&	horarioDisponivel.getHorarioAula().equals(slot.getHorario().getHorarioAula())) {
    					    					
    					slot.setApto(true);
    					acertou = true;
    					this.aptidao += 1;
    					    					
    				} 
    			}
    			if(!acertou) {
    				slot.setApto(false);
    				//this.aptidao -= 1;
    				
    			}
    		}
    	}
       
    }
    
    /**
     * Gera um novo hor�rio, em uma posi��o ainda n�o ocupada na grade hor�ria, para um slot
     */
    public Horario geraHorario() {
    	Random random = new Random();
    	
    	Integer indiceDia;
    	Integer indiceHorario;
    	String horarioDeAula;
    	
    	do {
			indiceDia = random.nextInt(Dados.getDiasDaSemana().length);
			
			indiceHorario = random.nextInt(Dados.getHorariosDeAula().length);

			horarioDeAula = Dados.getHorarioDeAula(indiceHorario);
			
			//Verifica se o horário gerado já existe 
    	} while (existsSlot(indiceDia, horarioDeAula));
    	
    	return new Horario(horarioDeAula, Dados.getDiaDaSemana(indiceDia));
    }
    
    /**
     * @return verdadeiro, se j� existir um slot alocado naquele determinado hor�rio. Falso, caso contr�rio
     */
    public boolean existsSlot(int indiceDia, String horarioDeAula) {
    	//varre os slots do dia indicado pela variável indiceDia
	    for(Slot slot: this.grade.get(indiceDia)) {
	    	//Verrifica se o horário da aula é igual ao que será alocado no slot
	    	if(slot.getHorario().getHorarioAula().equals(horarioDeAula)) {
	    		return true;
	    	}
	    }
    	return false;
    }
    
    public int getAptidao() {
        return this.aptidao;
    }
    
    public List<List<Slot>> getGenes() {
        return this.grade;
    }

    @Override
	public int compareTo(Individuo individuo) {
		if(this.aptidao > individuo.getAptidao()) {
			return 1;
		} else if(this.aptidao < individuo.getAptidao()) {
			return -1;
		}
		return 0;
	}
    
}