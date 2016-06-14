package br.ufpb.ia.ag.algorithm;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.ufpb.ia.ag.entity.Dados;
import br.ufpb.ia.ag.entity.Disciplina;
import br.ufpb.ia.ag.entity.Individuo;
import br.ufpb.ia.ag.entity.Populacao;
import br.ufpb.ia.ag.entity.Slot;

public class Algoritmo {

    private static double taxaDeCrossover;
    private static double taxaDeMutacao;
    private static List<Disciplina> disciplinas = new ArrayList<Disciplina>();
    
    public static Populacao novaGeracao(Populacao populacao, boolean elitismo) {
		
        Random random = new Random();
        //Nova população do mesmo tamanho da antiga
        Populacao novaPopulacao = new Populacao(populacao.getTamPopulacao());

        //Se tiver elitismo, mantém o melhor indivíduo da geração atual
        if (elitismo) {
            novaPopulacao.adicionarIndividuo(populacao.getIndividuo(0));
        }
        
        //Insere novos indivíduos na nova população, até atingir o tamanho máximo
        while (novaPopulacao.getNumIndividuos() < novaPopulacao.getTamPopulacao()) {
            //Seleciona os 2 pais por torneio
            Individuo[] pais = selecaoTorneio(populacao);

            Individuo[] filhos = new Individuo[2];

            //Verifica a taxa de crossover: se dentro da taxa, realiza o crossover, se não, mantém os pais selecionados para a próxima geração
            if (random.nextDouble() <= taxaDeCrossover) {
                filhos = crossover(pais[1], pais[0]);
            } else {
                filhos[0] = new Individuo(pais[0].getGenes());
                filhos[1] = new Individuo(pais[1].getGenes());
            }
            //Adiciona os filhos na nova geração
            novaPopulacao.adicionarIndividuo(filhos[0]);
            novaPopulacao.adicionarIndividuo(filhos[1]);
        }
        //Ordena a nova população
        novaPopulacao.ordenarPopulacao(); // pode ser um compareTo (implementar comparable)
        return novaPopulacao;
    }
    
    public static Individuo[] selecaoTorneio(Populacao populacao) {
        Random random = new Random();
        Populacao populacaoIntermediaria = new Populacao(3);
        
        //Seleciona 3 indivíduos aleatoriamente na população
        populacaoIntermediaria.adicionarIndividuo(populacao.getIndividuo(random.nextInt(populacao.getTamPopulacao())));
        populacaoIntermediaria.adicionarIndividuo(populacao.getIndividuo(random.nextInt(populacao.getTamPopulacao())));
        populacaoIntermediaria.adicionarIndividuo(populacao.getIndividuo(random.nextInt(populacao.getTamPopulacao())));

        //Ordena a população através da aptidão dos indivíduos
        populacaoIntermediaria.ordenarPopulacao();

        Individuo[] pais = new Individuo[2];

        //Seleciona os 2 melhores desta população intermediária
        pais[0] = populacaoIntermediaria.getIndividuo(0);
        pais[1] = populacaoIntermediaria.getIndividuo(1);

        return pais;
    }
    
    /**
     * Gera o crossover a partir de dois indivíduos (pais)
     * @return dois novos indivíduos gerados a partir de combinações dos genes dos seus pais
     */
    public static Individuo[] crossover(Individuo individuo1, Individuo individuo2) {
        Random random = new Random();
        int indiceDia;
        Slot slot;
        List<Slot> slotsFilho1 = new ArrayList<Slot>();
        List<Slot> slotsFilho2 = new ArrayList<Slot>();
        
        List<String> tarefasNaoAlocadasFilho1 = getDisciplinas(); 
        List<String> tarefasNaoAlocadasFilho2 = getDisciplinas(); 
        
        //Lista os slots dos genes dos pais 
        List<Slot> slotsPai1 = listarSlots(individuo1.getGenes());
    	List<Slot> slotsPai2 = listarSlots(individuo2.getGenes());
        
        //sorteia o ponto de corte
        int pontoCorte1 = random.nextInt((individuo1.getGenes().size()/2) -2) + 1;
        int pontoCorte2 = random.nextInt((individuo1.getGenes().size()/2) -2) + individuo1.getGenes().size()/2;
        //int pontoCorte1 = random.nextInt(2)+2;
        //int pontoCorte2 = pontoCorte1+random.nextInt(2)+2;
                
        Individuo[] filhos = new Individuo[2];

        //Cria os genes dos filhos
        List<List<Slot>> geneFilho1 = new ArrayList<List<Slot>>();
        List<List<Slot>> geneFilho2 = new ArrayList<List<Slot>>();
    	//Inicializa as listas que representam os dias da semana
    	for(int i = 0; i < Dados.getQuantidadeDias(); i++) {
    		geneFilho1.add(new ArrayList<Slot>());
    		geneFilho2.add(new ArrayList<Slot>());
    	}  

        int i = 0;
        //Realiza o corte e cria os novos genes dos filhos
        //Filho 1 recebe primeira parte do pai 1 e Filho 2 recebe primeira parte do pai 2
        for(i=0; i < pontoCorte1; i++) {
        	slot = slotsPai1.get(i);
        	indiceDia = Dados.getIndiceDiaDaSemana(slot.getHorario().getDiaDaSemana());
        	//Se o slot do pai ainda nï¿½o existir no filho 1, ele serï¿½ alocado 
        	//Testa o slot para que nï¿½o haja tarefas repetidas em uma mesma grade
        	if(validaAlocacao(slot.getTarefa(), tarefasNaoAlocadasFilho1)) {
        		avaliarHorario(slot.getHorario(), slotsFilho1);
            	geneFilho1.get(indiceDia).add(slot);
                slotsFilho1.add(slot);
            	//remover o slot da lista de tarefas nao alocadas
        	} 
        	else {
        		validaAlocacao(slot.getTarefa(), tarefasNaoAlocadasFilho2);
        		avaliarHorario(slot.getHorario(), slotsFilho2);
        		geneFilho2.get(indiceDia).add(slot);
        		slotsFilho2.add(slot);
        	}
        	
        	slot = slotsPai2.get(i);
        	indiceDia = Dados.getIndiceDiaDaSemana(slot.getHorario().getDiaDaSemana());
        	//Se o slot do pai ainda nï¿½o existir no filho 2, ele serï¿½ alocado 
        	//Testa o slot para que nï¿½o haja tarefas repetidas em uma mesma grade
        	if(validaAlocacao(slot.getTarefa(), tarefasNaoAlocadasFilho2)) {
        		avaliarHorario(slot.getHorario(), slotsFilho2);
            	geneFilho2.get(indiceDia).add(slot);
                slotsFilho2.add(slot);
        	} 
        	else {
        		validaAlocacao(slot.getTarefa(), tarefasNaoAlocadasFilho1);
        		avaliarHorario(slot.getHorario(), slotsFilho1);
        		geneFilho1.get(indiceDia).add(slot);
        		slotsFilho1.add(slot);
        	}
        }
        
        //Filho 1 recebe segunda parte do pai 2 e Filho 2 recebe segunda parte do pai 1
        for(i = pontoCorte1; i < pontoCorte2; i++) {
        	slot = slotsPai2.get(i);
        	indiceDia = Dados.getIndiceDiaDaSemana(slot.getHorario().getDiaDaSemana());
        	//Se o slot do pai ainda nï¿½o existir no filho 1, ele serï¿½ alocado 
        	//Testa o slot para que nï¿½o haja tarefas repetidas em uma mesma grade
        	if(validaAlocacao(slot.getTarefa(), tarefasNaoAlocadasFilho1)) {
        		avaliarHorario(slot.getHorario(), slotsFilho1);
            	geneFilho1.get(indiceDia).add(slot);
                slotsFilho1.add(slot);
        	} 
        	else {
        		validaAlocacao(slot.getTarefa(), tarefasNaoAlocadasFilho2);
        		avaliarHorario(slot.getHorario(), slotsFilho2);
        		geneFilho2.get(indiceDia).add(slot);
        		slotsFilho2.add(slot);
        	}
        	
        	slot = slotsPai1.get(i);
        	indiceDia = Dados.getIndiceDiaDaSemana(slot.getHorario().getDiaDaSemana());
        	//Se o slot do pai ainda nï¿½o existir no filho 2, ele serï¿½ alocado 
        	//Testa o slot para que nï¿½o haja tarefas repetidas em uma mesma grade
        	if(validaAlocacao(slot.getTarefa(), tarefasNaoAlocadasFilho2)) {
        		avaliarHorario(slot.getHorario(), slotsFilho2);
            	geneFilho2.get(indiceDia).add(slot);
                slotsFilho2.add(slot);
        	} 
        	else {
        		validaAlocacao(slot.getTarefa(), tarefasNaoAlocadasFilho1);
        		avaliarHorario(slot.getHorario(), slotsFilho1);
        		geneFilho1.get(indiceDia).add(slot);
        		slotsFilho1.add(slot);
        	}
        }
        
        //Filho 1 recebe terceira parte do pai 1 e Filho 2 recebe terceira parte do pai 2
        for(i = pontoCorte2; i < getQuantidadeDisciplinas(); i++) {
        	slot = slotsPai1.get(i);
        	indiceDia = Dados.getIndiceDiaDaSemana(slot.getHorario().getDiaDaSemana());
        	//Se o slot do pai ainda não existir no filho 1, ele será alocado 
        	//Testa o slot para que não haja tarefas repetidas em uma mesma grade
        	if(validaAlocacao(slot.getTarefa(), tarefasNaoAlocadasFilho1)) {
        		avaliarHorario(slot.getHorario(), slotsFilho1);
            	geneFilho1.get(indiceDia).add(slot);
                slotsFilho1.add(slot);
        	} 
        	else {
        		validaAlocacao(slot.getTarefa(), tarefasNaoAlocadasFilho2);
        		avaliarHorario(slot.getHorario(), slotsFilho2);
        		geneFilho2.get(indiceDia).add(slot);
        		slotsFilho2.add(slot);
        	}
        	
        	slot = slotsPai2.get(i);
        	indiceDia = Dados.getIndiceDiaDaSemana(slot.getHorario().getDiaDaSemana());
        	//Se o slot do pai ainda não existir no filho 2, ele será alocado 
        	//Testa o slot para que não haja tarefas repetidas em uma mesma grade
        	if(validaAlocacao(slot.getTarefa(), tarefasNaoAlocadasFilho2)) {
        		avaliarHorario(slot.getHorario(), slotsFilho2);
            	geneFilho2.get(indiceDia).add(slot);
                slotsFilho2.add(slot);
        	} 
        	else {
        		validaAlocacao(slot.getTarefa(), tarefasNaoAlocadasFilho1);
        		avaliarHorario(slot.getHorario(), slotsFilho1);
        		geneFilho1.get(indiceDia).add(slot);
        		slotsFilho1.add(slot);
        	}

        }
        
        //Cria o novo indivíduo com os genes dos pais
        filhos[0] = new Individuo(geneFilho1);
        filhos[1] = new Individuo(geneFilho2);
        return filhos;
    }
    
    /**
     * Verifica se a tarefa já existe na lista de tarefas não alocadas, caso exista, remove a tarefa em questão
     * @return Verdadeiro, se a lista de tarefas não alocadas possuir a tarefa passada. Falso, caso contrário
     */
    public static boolean validaAlocacao(Disciplina disciplina, List<Disciplina> disciplinasNaoAlocadas) {
    	for(Disciplina d: disciplinasNaoAlocadas) {
    		if(disciplina.equals(d.getNome().toUpperCase())) {   
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
    	for(Slot s: slots) {
    		if(s.getHorario().getDiaDaSemana().toUpperCase().equals(horario.getDiaDaSemana().toUpperCase())
    				&& s.getHorario().getHoraInicio().equals(horario.getHoraInicio())) {
    			
    			horario.setDiaDaSemana(Dados.getDiaDaSemana(random.nextInt(Dados.getQuantidadeDias())));
    			Integer horaInicio = random.nextInt(Dados.getHorarioMaximo());
    			Integer horaFim = horaInicio+1;
    			
    			horario.setHoraInicio(horaInicio.toString());
    			horario.setHoraFim(horaFim.toString());
    		}
    	}
    }
    
    /**
     * Lista os slots de um determinado individuo
     */
    public static List<Slot> listarSlots(List<List<Slot>> individuo) {
    	List<Slot> slots = new ArrayList<Slot>();
    	for(int i = 0; i < individuo.size(); i++) {
    		for(Slot slot: individuo.get(i)) {
    			slots.add(slot);
    		}
    	}
    	return slots;
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
    	// A aptdão máxima é a quantidade de disciplinas existentes
		return disciplinas.size();
	}
  
}