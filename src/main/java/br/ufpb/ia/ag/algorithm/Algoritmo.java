package br.ufpb.ia.ag.algorithm;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.ufpb.ia.ag.entity.Dados;
import br.ufpb.ia.ag.entity.Disciplina;
import br.ufpb.ia.ag.entity.Horario;
import br.ufpb.ia.ag.entity.Individuo;
import br.ufpb.ia.ag.entity.Populacao;
import br.ufpb.ia.ag.entity.Slot;

public class Algoritmo {

    private static double taxaDeCrossover;
    private static double taxaDeMutacao;
    private static List<Disciplina> disciplinas = new ArrayList<Disciplina>();
    
    public static Populacao novaGeracao(Populacao populacao, boolean elitismo) {
        Random random = new Random();
        //Nova popula��o do mesmo tamanho da antiga
        Populacao novaPopulacao = new Populacao(); //parametro: populacao.getTamPopulacao()

        //Se tiver elitismo, mantém o melhor indivíduo da geração atual
        if (elitismo) {
            novaPopulacao.adicionarIndividuo(populacao.getIndividuo(0));
        }
        
        //Insere novos indivíduos na nova população, até atingir o tamanho máximo de indivíduos
        while (novaPopulacao.getTamPopulacao() < Dados.getTamanhoMaximoPopulacao()) {
        	
            //Seleciona os 2 pais por torneio
            Individuo[] pais = selecaoTorneio(populacao);

            Individuo[] filhos = new Individuo[2];

            //Verifica a taxa de crossover: se dentro da taxa, realiza o crossover, senão, mantém os pais selecionados para a próxima geração
            if (random.nextDouble() <= taxaDeCrossover) {
                filhos = crossover(pais[1], pais[0]);
            } else {
                filhos[0] = new Individuo(pais[0].getGenes());
                filhos[1] = new Individuo(pais[1].getGenes());
            }
            //Adiciona os filhos na nova gera��o
            novaPopulacao.adicionarIndividuo(filhos[0]);
            novaPopulacao.adicionarIndividuo(filhos[1]);
            
        }
        
        //Ordena a nova popula��o
        novaPopulacao.ordenarPopulacao(); 
        
        return novaPopulacao;
    }
    
    public static Individuo[] selecaoTorneio(Populacao populacao) {
        Random random = new Random();
        Populacao populacaoIntermediaria = new Populacao(); //Populacao populacaoIntermediaria = new Populacao(3);
        
        //Seleciona 3 indivíduos aleatoriamente na população
        populacaoIntermediaria.adicionarIndividuo(populacao.getIndividuo(random.nextInt(populacao.getTamPopulacao())));
        populacaoIntermediaria.adicionarIndividuo(populacao.getIndividuo(random.nextInt(populacao.getTamPopulacao())));
        populacaoIntermediaria.adicionarIndividuo(populacao.getIndividuo(random.nextInt(populacao.getTamPopulacao())));

        //Ordena a popula��o atrav�s da aptid�o dos indiv�duos
        populacaoIntermediaria.ordenarPopulacao();

        Individuo[] pais = new Individuo[2];

        //Seleciona os 2 melhores desta popula��o intermedi�ria
        pais[0] = populacaoIntermediaria.getIndividuo(0);
        pais[1] = populacaoIntermediaria.getIndividuo(1);

        return pais;
    }
    
    /**
     * Gera o crossover a partir de dois indiv�duos (pais)
     * @return dois novos indiv�duos gerados a partir de combina��es dos genes dos seus pais
     */
    public static Individuo[] crossover(Individuo individuo1, Individuo individuo2) {
        Random random = new Random();
        int indiceDia;
        Slot slot;
        List<Slot> slotsFilho1 = new ArrayList<Slot>();
        List<Slot> slotsFilho2 = new ArrayList<Slot>();
                
        List<Disciplina> disciplinasNaoAlocadasFilho1 = new ArrayList<Disciplina>();
    	
    	//Adiciona as disciplinas que deverão ser alocadas na lista auxiliar
    	for(Disciplina disciplina: Algoritmo.getDisciplinas()) {
    		disciplinasNaoAlocadasFilho1.add(disciplina);
    	}
    	
    	List<Disciplina> disciplinasNaoAlocadasFilho2 = new ArrayList<Disciplina>();
    	
    	//Adiciona as disciplinas que deverão ser alocadas na lista auxiliar
    	for(Disciplina disciplina: Algoritmo.getDisciplinas()) {
    		disciplinasNaoAlocadasFilho2.add(disciplina);
    	}
        
        //Lista os slots dos genes dos pais 
        List<Slot> slotsPai1 = listarSlots(individuo1.getGenes());
    	List<Slot> slotsPai2 = listarSlots(individuo2.getGenes());
        
        //Estabelece os pontos de corte 
        int pontoCorte1 = 2; 					// random.nextInt((individuo1.getGenes().size()/2) -1) + 1
        int pontoCorte2 = 3;					// random.nextInt((individuo1.getGenes().size()/2) -1) + individuo1.getGenes().size()/2
        //int pontoCorte1 = random.nextInt(2)+2;
        //int pontoCorte2 = pontoCorte1+random.nextInt(2)+2;
                
        Individuo[] filhos = new Individuo[2];

        //Cria os genes dos filhos
        List<List<Slot>> genesFilho1 = new ArrayList<List<Slot>>();
        List<List<Slot>> genesFilho2 = new ArrayList<List<Slot>>();
    	//Inicializa as listas que representam os dias da semana
    	for(int i = 0; i < Dados.getDiasDaSemana().length; i++) {
    		genesFilho1.add(new ArrayList<Slot>());
    		genesFilho2.add(new ArrayList<Slot>());
    	}  

        int i = 0;
        //Realiza o corte e cria os novos genes dos filhos
        //Filho 1 recebe primeira parte do pai 1 e Filho 2 recebe primeira parte do pai 2
        for(i=0; i < pontoCorte1; i++) {  
        	slot = slotsPai1.get(i);
        	indiceDia = Dados.getIndiceDiaDaSemana(slot.getHorario().getDiaDaSemana());  
        	//Se o slot do pai ainda n�o existir no filho 1, ele ser� alocado 
        	//Testa o slot para que n�o haja disciplinas repetidas em uma mesma grade
        	if(validaAlocacao(slot.getDisciplina(), disciplinasNaoAlocadasFilho1)) {
        		avaliarHorario(slot.getHorario(), slotsFilho1);
            	genesFilho1.get(indiceDia).add(slot);
                slotsFilho1.add(slot);
            	//remover o slot da lista de tarefas nao alocadas
        	} 
        	else {
        		validaAlocacao(slot.getDisciplina(), disciplinasNaoAlocadasFilho2);
        		avaliarHorario(slot.getHorario(), slotsFilho2);
        		genesFilho2.get(indiceDia).add(slot);
        		slotsFilho2.add(slot);
        	}
        	
        	slot = slotsPai2.get(i);
        	indiceDia = Dados.getIndiceDiaDaSemana(slot.getHorario().getDiaDaSemana());
        	//Se o slot do pai ainda n�o existir no filho 2, ele ser� alocado 
        	//Testa o slot para que n�o haja tarefas repetidas em uma mesma grade
        	if(validaAlocacao(slot.getDisciplina(), disciplinasNaoAlocadasFilho2)) {
        		avaliarHorario(slot.getHorario(), slotsFilho2);
            	genesFilho2.get(indiceDia).add(slot);
                slotsFilho2.add(slot);
        	} 
        	else {
        		validaAlocacao(slot.getDisciplina(), disciplinasNaoAlocadasFilho1);
        		avaliarHorario(slot.getHorario(), slotsFilho1);
        		genesFilho1.get(indiceDia).add(slot);
        		slotsFilho1.add(slot);
        	}
        }
        
        //Filho 1 recebe segunda parte do pai 2 e Filho 2 recebe segunda parte do pai 1
        for(i = pontoCorte1; i < pontoCorte2; i++) {
        	slot = slotsPai2.get(i);
        	indiceDia = Dados.getIndiceDiaDaSemana(slot.getHorario().getDiaDaSemana());
        	//Se o slot do pai ainda n�o existir no filho 1, ele ser� alocado 
        	//Testa o slot para que n�o haja tarefas repetidas em uma mesma grade
        	if(validaAlocacao(slot.getDisciplina(), disciplinasNaoAlocadasFilho1)) {
        		avaliarHorario(slot.getHorario(), slotsFilho1); //Muda o horário do slot caso o horário já esteja preenchido na grade do filho 
            	genesFilho1.get(indiceDia).add(slot);
                slotsFilho1.add(slot);
        	} 
        	else {
        		validaAlocacao(slot.getDisciplina(), disciplinasNaoAlocadasFilho2);
        		avaliarHorario(slot.getHorario(), slotsFilho2);
        		genesFilho2.get(indiceDia).add(slot);
        		slotsFilho2.add(slot);
        	}
        	
        	slot = slotsPai1.get(i);
        	indiceDia = Dados.getIndiceDiaDaSemana(slot.getHorario().getDiaDaSemana());
        	//Se o slot do pai ainda n�o existir no filho 2, ele ser� alocado 
        	//Testa o slot para que n�o haja tarefas repetidas em uma mesma grade
        	if(validaAlocacao(slot.getDisciplina(), disciplinasNaoAlocadasFilho2)) {
        		avaliarHorario(slot.getHorario(), slotsFilho2);
            	genesFilho2.get(indiceDia).add(slot);
                slotsFilho2.add(slot);
        	} 
        	else {
        		validaAlocacao(slot.getDisciplina(), disciplinasNaoAlocadasFilho1);
        		avaliarHorario(slot.getHorario(), slotsFilho1);
        		genesFilho1.get(indiceDia).add(slot);
        		slotsFilho1.add(slot);
        	}
        }
        
        //Filho 1 recebe terceira parte do pai 1 e Filho 2 recebe terceira parte do pai 2
        for(i = pontoCorte2; i < disciplinas.size(); i++) {
        	slot = slotsPai1.get(i);
        	indiceDia = Dados.getIndiceDiaDaSemana(slot.getHorario().getDiaDaSemana());
        	//Se o slot do pai ainda n�o existir no filho 1, ele ser� alocado 
        	//Testa o slot para que n�o haja tarefas repetidas em uma mesma grade
        	if(validaAlocacao(slot.getDisciplina(), disciplinasNaoAlocadasFilho1)) {
        		avaliarHorario(slot.getHorario(), slotsFilho1);
            	genesFilho1.get(indiceDia).add(slot);
                slotsFilho1.add(slot);
        	} 
        	else {
        		validaAlocacao(slot.getDisciplina(), disciplinasNaoAlocadasFilho2);
        		avaliarHorario(slot.getHorario(), slotsFilho2);
        		genesFilho2.get(indiceDia).add(slot);
        		slotsFilho2.add(slot);
        	}
        	
        	slot = slotsPai2.get(i);
        	indiceDia = Dados.getIndiceDiaDaSemana(slot.getHorario().getDiaDaSemana());
        	//Se o slot do pai ainda n�o existir no filho 2, ele ser� alocado 
        	//Testa o slot para que n�o haja tarefas repetidas em uma mesma grade
        	if(validaAlocacao(slot.getDisciplina(), disciplinasNaoAlocadasFilho2)) {
        		avaliarHorario(slot.getHorario(), slotsFilho2);
            	genesFilho2.get(indiceDia).add(slot);
                slotsFilho2.add(slot);
        	} 
        	else {
        		validaAlocacao(slot.getDisciplina(), disciplinasNaoAlocadasFilho1);
        		avaliarHorario(slot.getHorario(), slotsFilho1);
        		genesFilho1.get(indiceDia).add(slot);
        		slotsFilho1.add(slot);
        	}

        }
        
        //Cria o novo indiv�duo com os genes dos pais
        filhos[0] = new Individuo(genesFilho1);
        filhos[1] = new Individuo(genesFilho2);
        return filhos;
    }
    
    /**
     * Verifica se a disciplina já existe na lista de disciplinas não alocadas, caso exista, remove a disciplina em quest�o
     * @return Verdadeiro, se a lista de disciplinas não alocadas possuir a disciplina passada. Falso, caso contrário
     */
    public static boolean validaAlocacao(Disciplina disciplina, List<Disciplina> disciplinasNaoAlocadas) {
    	for(Disciplina d: disciplinasNaoAlocadas) {
    		if(disciplina.getNome().equals(d.getNome())) {   
    			disciplinasNaoAlocadas.remove(d);
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Altera o horário do slot se ele já estiver ocupado na grade
     */
    public static void avaliarHorario(Horario horario, List<Slot> slots) {
    	Random random = new Random();
    	boolean mudouHorario;
    	
    	for(Slot s: slots) {
    		if(s.getHorario().getDiaDaSemana().equals(horario.getDiaDaSemana())
    				&& s.getHorario().getHorarioAula().equals(horario.getHorarioAula())) {
    			
    			//Muda o dia da semana do hor�rio
    			horario.setDiaDaSemana(Dados.getDiaDaSemana(random.nextInt(Dados.getDiasDaSemana().length)));
    			
    			//Muda o hor�rio da aula do hor�rio
    			horario.setHorarioAula(Dados.getHorarioDeAula(random.nextInt(Dados.getHorariosDeAula().length)));
    			
    			mudouHorario = true;
    			
    			while(mudouHorario) {
    				mudouHorario = false;
    				for(Slot s1: slots) {
    					if(s1.getHorario().getDiaDaSemana().equals(horario.getDiaDaSemana())
    		    				&& s1.getHorario().getHorarioAula().equals(horario.getHorarioAula())) {
			    			//Muda o dia da semana do hor�rio
			    			horario.setDiaDaSemana(Dados.getDiaDaSemana(random.nextInt(Dados.getDiasDaSemana().length)));
			    			
			    			//Muda o hor�rio da aula do hor�rio
			    			horario.setHorarioAula(Dados.getHorarioDeAula(random.nextInt(Dados.getHorariosDeAula().length)));
			    			
			    			mudouHorario = true;
			    		
    					}
	    			}
    			}
    		}
    	}
    }
    
    /**
     * Lista os slots de um determinado individuo
     */
    public static List<Slot> listarSlots(List<List<Slot>> individuo) {
    	List<Slot> aux = new ArrayList<Slot>();
    	for(List<Slot> slots: individuo) {
    		for(Slot slot: slots) {
    			aux.add(slot);
    		}
    	}
    	return aux;
    }

    public static double getTaxaDeCrossover() {
        return taxaDeCrossover;
    }

    public static void setTaxaDeCrossover(double taxaDeCrossover) {
        Algoritmo.taxaDeCrossover = taxaDeCrossover;
    }

    public static double getTaxaDeMutacao() {
        return taxaDeMutacao;
    }

    public static void setTaxaDeMutacao(double taxaDeMutacao) {
        Algoritmo.taxaDeMutacao = taxaDeMutacao;
    }
    
    
    public static List<Disciplina> getDisciplinas() {
		return disciplinas;
	}
	

	public static void setDisciplinas(List<Disciplina> disciplinas) {
		Algoritmo.disciplinas = disciplinas;
	}

    public static int getAptidaoMaxima() {
    	// A aptd�o m�xima � a quantidade de disciplinas existentes
		return disciplinas.size();
	}
  
}