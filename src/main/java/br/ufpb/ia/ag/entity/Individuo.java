package br.ufpb.ia.ag.entity;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.ufpb.ia.ag.algorithm.Algoritmo;

public class Individuo {

	//Os genes do indivï¿½duo ï¿½ uma lista bidimensional, que representa uma grade horï¿½ria composta por slots 	
	private List<List<Slot>> grade;
	private int aptidao = 0;   

    /**
     * Gera um indivï¿½duo aleatï¿½rio
     */
    public Individuo(int numGenes) {  
    	this.grade = new ArrayList<List<Slot>>();
    	Random random = new Random();
    	List<Disciplina> disciplinasNaoAlocadas = Algoritmo.getDisciplinas();
    	Horario horario;
    	
    	//Inicializa as listas que representam os dias da semana
    	for(int i = 0; i < Dados.getQuantidadeDias(); i++) {
    		this.grade.add(new ArrayList<Slot>());
    	} 
    	
    	//Aloca todas as tarefas de modo aleatório na grade horária
    	for (int i = 0; i < Algoritmo.getDisciplinas().size(); i++) {
    		horario = geraHorario();
	        
	        //Adiciona o slot contendo a tarefa e seu respectivo horário de realização
	        this.grade.get(Dados.getIndiceDiaDaSemana(horario.getDiaDaSemana())).add(
	        								new Slot(disciplinasNaoAlocadas.remove(random.nextInt(disciplinasNaoAlocadas.size())), horario));
        }
    	
        geraAptidao(); 
    	 
    }

	/**
     *  Cria um indivï¿½duo com os genes definidos
     */
    public Individuo(List<List<Slot>> genes) {   
    	Random random = new Random();
        this.grade = genes;
    
        //Se for mutar, inverte a posição entre dois genes
        if (random.nextDouble() <= Algoritmo.getTaxaDeMutacao()) {
        	
        	int indiceDia1 = random.nextInt(Dados.getQuantidadeDias());
        	//Loop enquanto o dia sorteado não possuir nenhum slot alocado
        	while(genes.get(indiceDia1).size() == 0){
        		indiceDia1 = random.nextInt(Dados.getQuantidadeDias());
        	}
        	int indiceDia2 = random.nextInt(Dados.getQuantidadeDias());
        	//Loop enquanto o dia sorteado não possuir nenhum slot alocado
        	while(genes.get(indiceDia2).size() == 0){
        		indiceDia2 = random.nextInt(Dados.getQuantidadeDias());
        	}
        	
        	int posicaoAleatoria1 = random.nextInt(genes.get(indiceDia1).size());
        	int posicaoAleatoria2 = random.nextInt(genes.get(indiceDia2).size());
        	
        	Slot slot1 = genes.get(indiceDia1).get(posicaoAleatoria1);
        	Slot slot2 = genes.get(indiceDia2).get(posicaoAleatoria2);
        	
        	//Substitui a posição de dois genes aleatórios
        	genes.get(indiceDia1).set(posicaoAleatoria1, slot2);
        	genes.get(indiceDia2).set(posicaoAleatoria2, slot1);
        	
        }
        this.grade = genes;
        geraAptidao();
    }

    /**
     * Calcula o valor de aptidão do indivíduo
     */
    private void geraAptidao() {
    	this.aptidao = 0;
    	boolean acertou;
    	
    	for(int i = 0; i < this.grade.size(); i++) {
    		for(Slot slot: this.grade.get(i)) {
    			acertou = false;
    			//Varre a lista de horários preferidos do professor
    			for(Horario horarioDisponivel: slot.getDisciplina().getProfessor().getHorariosPreferidos()) {
    				// Verifica se o horário do slot é um horário de preferência do professor
    				if(horarioDisponivel.getDiaDaSemana().equals(slot.getHorario().getDiaDaSemana())
    					&&	horarioDisponivel.getHorarioAula().equals(slot.getHorario().getHorarioAula())) {
    					
    					slot.setApto(true);
    					acertou = true;
    					this.aptidao += 1;
    					
    				} //else 
    			}
    			if(!acertou) {
    				slot.setApto(false);
    				this.aptidao -= 1;
    			}
    		}
    	}
       
    }
    
    /**
     * @return verdadeiro, se já existir um slot alocado naquele determinado horário. Falso, caso contrário
     */
    public boolean existsSlot(int indiceDia, String horario) {
	    for(Slot slot: this.grade.get(indiceDia)) {
	    	if(slot.getHorario().equals(horario)) {
	    		return true;
	    	}
	    }
    	return false;
    }
    
    /**
     * Gera um novo horário, em uma posição ainda não ocupada na grade horária, para um slot
     */
    public Horario geraHorario() {
    	Random random = new Random();
    	
    	Integer indiceDia;
    	Integer indiceHorario;
    	String horario;
    	
    	do {
			indiceDia = random.nextInt(Dados.getQuantidadeDias());
			indiceHorario = random.nextInt(Dados.getHorarioMaximo());

			horario = Dados.getHorarioDeAula(indiceHorario);
			
		//Verifica se o horário gerado já existe 
    	} while (existsSlot(indiceDia, horario));
    	
    	return new Horario(horario, Dados.getDiaDaSemana(indiceDia));
    }
    
    public int getAptidao() {
        return this.aptidao;
    }
    
    public List<List<Slot>> getGenes() {
        return this.grade;
    }
    
}