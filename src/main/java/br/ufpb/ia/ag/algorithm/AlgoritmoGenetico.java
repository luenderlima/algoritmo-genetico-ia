package br.ufpb.ia.ag.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.ufpb.ia.ag.entity.Dados;
import br.ufpb.ia.ag.entity.Disciplina;
import br.ufpb.ia.ag.entity.Horario;
import br.ufpb.ia.ag.entity.Individuo;
import br.ufpb.ia.ag.entity.Populacao;
import br.ufpb.ia.ag.entity.Professor;
import br.ufpb.ia.ag.entity.Slot;

public class AlgoritmoGenetico {
	
	public static void main(String[] args) {

		Random random = new Random();
    	String[] nomesProfessores = {"Eduardo", "Yuska", "Marcus", "Yuri 2", "Hacks", "Yuri", "Luiz"};
    								/*
    								"Ayla", "RR", "Juliana Aragão", "Ana Liz", "Geraldo", "Jorge", "Renata",
    								"Vanessa", "Gabriela", "Carlos Alberto", "Fabrício", "Eliane", "Juliana Saraiva", "Rafael", "Wagner", 
    								"Scaico", "Angeluce", "Joelson", "Flávia", "Théofilo", "Osicleide"
    								 */
    	
    	String[] nomesDisciplinas = {"PAS II", "GQS", "SD", "IA", "ASS", "DSC", "SAG"};
    				/*
    				"ADS", "LP", "IP", "ADM I", "ADM II", "PAS I", "IC", "ADM II", "FILOSOFIA", "�TICA", "SOCIOLOGIA", 
    				"POO", "EMPREENDEDORISMO", "SD", "CÁLCULO", "ME", "AL", "PE", "RC", "GRC", "BD I", "BD II", "AED I", "AED II", "ES", "ESA",
    				"ARQUITETURA 1", "ARQUITETURA II", "SO", "LÓGICA", "PARADIGMAS", "GPS", "GINF", "PESQUISA APLICADA", "IHC", "TCC", "ESTÁGIO", "LIBRAS", "INGLÊS",
    				"MODELAGEM", "PSCOLOGIA", "ESTÁGIO 1 - LCC", "ESTÁGIO 2 - LCC", "ESTÁGIO 3 - LCC", "ESTÁGIO 4 - LCC", "DIDÁTICA", "LUSIVAL"}; 
    				*/
    	    	
    	List<Disciplina> disciplinas = new ArrayList<Disciplina>();
    	
    	try {
    		int quantHorariosDePreferenciaProfessor = 8;
    		boolean cadastrou = false;
    		Disciplina disciplina;
    		List<Professor> professores = new ArrayList<Professor>();
    		
    		for(String nome: nomesProfessores) {
    			Professor professor = new Professor();
	        	professor.setNome(nome);
	        	
	        	//Cria e cadastra os hor�rios de prefer�ncia do professor
	        	for(int i=0; i < quantHorariosDePreferenciaProfessor; i++) {
	        		cadastrou = false;
	        		
	        		while(!cadastrou) {
		        		try {
			        		String dia = Dados.getDiaDaSemana(random.nextInt(Dados.getDiasDaSemana().length));
			        		String horarioAula = Dados.getHorarioDeAula(random.nextInt(Dados.getHorariosDeAula().length));
			        		
			        		Horario horario = new Horario(horarioAula, dia);
			        		
		        			professor.cadastrarHorarioPreferido(horario);
		        			cadastrou = true;
		        			System.out.println("Cadastrou o horário - dia da semana: "+horario.getDiaDaSemana()+" / horário aula: "+horario.getHorarioAula()
		        								+" para o professor "+ professor.getNome().toUpperCase());
		        		} catch(Exception e) {
		        			cadastrou = false;
		        		}
	        		}
	        		
	        		
		        			        			        	
	        	}
	        	professores.add(professor);
    		}
    		
			
			//Cadastra disciplinas e aloca os professores
    		int j=0; 
    		for(int i=0; i < nomesDisciplinas.length; i++) {
    			
    			if(j==professores.size()-1){
    				j=0;
    			} else {
    				j++;
    			}

		    	disciplina = new Disciplina(nomesDisciplinas[i]);
		    	disciplina.setProfessor(professores.get(j));

    			disciplinas.add(disciplina);
    			
    			System.out.println("Professor: "+disciplina.getProfessor().getNome()+" ministrará a disciplina: "+disciplina.getNome());
    		}
    			    	        
    		int cont = 0;
    		for(List<Slot> slots: executarAlgoritmoGenetico(disciplinas)) {
    			for(Slot slot: slots) {
    				System.out.println("Slot "+cont+" - Horário da aula: " + slot.getHorario().getDiaDaSemana() + " - " + slot.getHorario().getHorarioAula()+
    									"\nDisciplina: "+slot.getDisciplina().getNome()+", professor: "+slot.getDisciplina().getProfessor().getNome()+"\n\n");
    				
    				cont++;
    			}
    			
    		}
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    
    public static List<List<Slot>> executarAlgoritmoGenetico(List<Disciplina> disciplinas) {
    	
    	//Seta os valores para a execu��o do algoritmo
    	Algoritmo.setDisciplinas(disciplinas);
    	//Define a taxa de crossover do algoritmo
        Algoritmo.setTaxaDeCrossover(Dados.getTaxaDeCrossover());
        //Define a taxa de muta��o do algoritmo
        Algoritmo.setTaxaDeMutacao(Dados.getTaxaDeMutacao());
        //Eltismo: Manuten��o do melhor indiv�duo nas pr�ximas gera��es
        boolean eltismo = true;
        //Tamanho da popula��o que ser� criada
        int tamanhoPopulacao = Dados.getTamanhoMaximoPopulacao();
        //N�mero m�ximo de gera��es do algoritmo
        int numMaxGeracoes = Dados.getNumeroMaximoGeracoes();
        //Define o n�mero de genes do indiv�duo baseado na quantidadde de disciplinas que devem ser alocadas
        int numGenes = Algoritmo.getDisciplinas().size();
         
        //Cria a primeira popula��o aleatoriamente
        Populacao populacao = new Populacao(numGenes, tamanhoPopulacao);
        
        // Avalia a primeira popula��o gerada, verificando se a solu��o procurada foi encontrada
        boolean temSolucao = populacao.avaliarPopulacao(numGenes);   
        int geracao = 0;
                
        System.out.println("Iniciando... Aptid�o da solu��o: "+numGenes);
        
        //Loop at� a solu��o ser encontrada ou at� atingir o n�mero m�ximo de gera��es
        while (!temSolucao && geracao < numMaxGeracoes) {
            geracao++;
            
            //Cria uma nova popula��o
            populacao = Algoritmo.novaGeracao(populacao, eltismo);
                        
            //Imprime o melhor indiv�duo da popula��o
            System.out.println("Geraçãoo " + geracao + " | Aptidão: " + populacao.getIndividuo(0).getAptidao());
            
            //Avalia a nova gera��o criada, verificando se a soluu��o procurada foi encontrada 
            temSolucao = populacao.avaliarPopulacao(Algoritmo.getAptidaoMaxima());
            if(temSolucao);
            	System.err.println("TEM SOLUÇÃO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        
        if(temSolucao) {
        	System.out.println("\n\nRESULTADO OBTIDO ATRAVÉS DA SOLUÇÃO!");
        } if(geracao == numMaxGeracoes) {
        	System.out.println("\n\nRESULTADO OBTIDO ATRAVÉS DO NÚMERO MÁXIMO DE GERAÇÕES!");
        }
        
        int cont = 0;
        for(Individuo i: populacao.getIndividuos()) {
        	System.out.println("APTIDÃO DO INDIVÍDUO "+cont+": "+i.getAptidao());
        	cont++;
        }
        
        System.out.println("aptidão do resultado: "+populacao.getMelhorIndividuo().getAptidao());
        return populacao.getMelhorIndividuo().getGenes();

    }
}