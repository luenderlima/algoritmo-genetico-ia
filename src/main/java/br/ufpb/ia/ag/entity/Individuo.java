package br.ufpb.ia.ag.entity;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.ufpb.ia.ag.algorithm.Algoritmo;

public class Individuo implements Comparable<Individuo> {

	//Os genes do indivíduo é uma lista bidimensional, que representa uma grade horária composta por slots 	
	private List<List<Slot>> gradeHoraria;
	private int aptidao;   

    /**
     * Gera um indivíduo aleatório
     */
    public Individuo(int numGenes) {  
    	this.gradeHoraria = new ArrayList<List<Slot>>();
    	Random random = new Random();
    	List<Disciplina> disciplinasNaoAlocadas = new ArrayList<Disciplina>();
    	    	
    	//Adiciona as disciplinas que deverão ser alocadas na lista auxiliar
    	for(Disciplina disciplina: Algoritmo.getDisciplinas()) {
    		disciplinasNaoAlocadas.add(disciplina);
    	}
    	
    	//Inicializa as listas que representam os dias da semana
    	for(int i = 0; i < Dados.getDiasDaSemana().length; i++) {
    		this.gradeHoraria.add(new ArrayList<Slot>());
    	} 
    	
    	//Aloca todas as disciplinas de modo aleatório na grade horária
    	for (int i = 0; i < Algoritmo.getDisciplinas().size(); i++) {
    		Horario horario = geraHorario();
    		
	        //Adiciona o slot contendo a disciplina e o respectivo horário em que será ministrada
	        this.gradeHoraria.get(Dados.getIndiceDiaDaSemana(horario.getDiaDaSemana())).add(
	        								new Slot(disciplinasNaoAlocadas.remove(random.nextInt(disciplinasNaoAlocadas.size())), horario));
	        
        }
    	
        geraAptidao(); 
    	 
    }

	/**
     *  Cria um indivíduo com os genes definidos
     */
    public Individuo(List<List<Slot>> genes) {   
    	Random random = new Random();
        this.gradeHoraria = genes;
    
        //Se for realizar a mutação, inverte a posição entre dois genes
        if (random.nextDouble() <= Algoritmo.getTaxaDeMutacao()) {
        	int indiceDia1;
        	int indiceDia2;
        	int posicaoAleatoria1;
        	int posicaoAleatoria2;
        	
        	do {
	        	indiceDia1 = random.nextInt(Dados.getDiasDaSemana().length);
	        	
	        	//Enquanto o dia sorteado não possuir nenhum slot alocado
	        	while(genes.get(indiceDia1).size() == 0){
	        		indiceDia1 = random.nextInt(Dados.getDiasDaSemana().length);
	        	}
	        	
	        	indiceDia2 = random.nextInt(Dados.getDiasDaSemana().length);
	      
	        	//Enquanto o dia sorteado não possuir nenhum slot alocado
	        	while(genes.get(indiceDia2).size() == 0){
	        		indiceDia2 = random.nextInt(Dados.getDiasDaSemana().length);
	        	}
	        	
	        	posicaoAleatoria1 = random.nextInt(genes.get(indiceDia1).size());
	        	posicaoAleatoria2 = random.nextInt(genes.get(indiceDia2).size());
	        	
	        	//Verifica se não está pegando o mesmo gene
        	} while(indiceDia1 == indiceDia2 && posicaoAleatoria1 == posicaoAleatoria2);
        	
        	Slot slot1 = genes.get(indiceDia1).get(posicaoAleatoria1);
        	Slot slot2 = genes.get(indiceDia2).get(posicaoAleatoria2);
        	
        	//Atualiza os horários dos slots que serão trocados
        	String diaDaSemanaSlot1 = slot1.getHorario().getDiaDaSemana();
        	String horarioAulaSlot1 = slot1.getHorario().getHorarioAula();
        	
        	slot1.getHorario().setDiaDaSemana(slot2.getHorario().getDiaDaSemana());
        	slot1.getHorario().setHorarioAula(slot2.getHorario().getHorarioAula());
        	
        	slot2.getHorario().setDiaDaSemana(diaDaSemanaSlot1);
        	slot2.getHorario().setHorarioAula(horarioAulaSlot1);
        	
        	//Substitui a posição dos dois genes
        	genes.get(indiceDia1).set(posicaoAleatoria1, slot2);
        	genes.get(indiceDia2).set(posicaoAleatoria2, slot1);
        	
        }
        
        geraAptidao();
    }

    /**
     * Calcula o valor da aptidão do indivíduo
     */
    private void geraAptidao() {
    	this.aptidao = 0;
    	boolean acertou;
    	
    	for(List<Slot> slots: this.gradeHoraria) {
    		for(Slot slot: slots) {
    			acertou = false;
    			
    			//Varre a lista de horários preferidos do professor
    			for(Horario horarioPreferido: slot.getDisciplina().getProfessor().getHorariosPreferidos()) {
    				
    				// Verifica se o horário do slot é um horário de preferência do professor
    				if(horarioPreferido.getDiaDaSemana().equals(slot.getHorario().getDiaDaSemana())
    					&& horarioPreferido.getHorarioAula().equals(slot.getHorario().getHorarioAula())) {
    					    					
    					slot.setApto(true);
    					acertou = true;
    					this.aptidao++;
    				} 
    			}
    			if(!acertou) {
    				slot.setApto(false);
    			}
    		}
    	}
       
    }
    
    /**
     * Gera um novo horário, em uma posição ainda não ocupada na grade horária, para um slot
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
     * @return verdadeiro, se já existir um slot alocado naquele determinado horário. Falso, caso contrário
     */
    public boolean existsSlot(int indiceDia, String horarioDeAula) {
    	//varre os slots do dia indicado pela variável indiceDia
	    for(Slot slot: this.gradeHoraria.get(indiceDia)) {
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
        return this.gradeHoraria;
    }

    /**
     * A ordenação dos indivíduos será feita a partir do indivíduo que possui a maior aptidão para a menor 
     */
    @Override
	public int compareTo(Individuo individuo) {
		if(this.aptidao < individuo.getAptidao()) { 
			return 1;
		} else if(this.aptidao > individuo.getAptidao()) {
			return -1;
		}
		return 0;
	}
    
}