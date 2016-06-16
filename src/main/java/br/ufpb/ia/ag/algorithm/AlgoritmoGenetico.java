package br.ufpb.ia.ag.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.ufpb.ia.ag.entity.Dados;
import br.ufpb.ia.ag.entity.Disciplina;
import br.ufpb.ia.ag.entity.Horario;
import br.ufpb.ia.ag.entity.Populacao;
import br.ufpb.ia.ag.entity.Professor;
import br.ufpb.ia.ag.entity.Slot;

public class AlgoritmoGenetico {
	
	public static void main(String[] args) {

		Random random = new Random();
    	String[] nomesProfessores = {"Eduardo", "Yuska", "Marcus", "Yuri 2", "Hacks", "Yuri", "Luiz"};
    	
    	String[] nomesDisciplinas = {"PAS II", "GQS", "SD", "IA", "ASS", "DSC", "SAG"};
    	    	
    	List<Disciplina> disciplinas = new ArrayList<Disciplina>();
    	
    	try {
    		int quantHorariosDePreferenciaProfessor = 9;
    		boolean cadastrou = false;
    		Disciplina disciplina;
    		List<Professor> professores = new ArrayList<Professor>();
    		
    		for(String nome: nomesProfessores) {
    			Professor professor = new Professor();
	        	professor.setNome(nome);
	        	
	        	//Cria e cadastra os horários de preferência do professor
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
    		for(int i=0; i < nomesDisciplinas.length; i++) {
		    	disciplina = new Disciplina(nomesDisciplinas[i]);
		    	disciplina.setProfessor(professores.get(i)); 

    			disciplinas.add(disciplina);
    			
    			System.out.println("Professor: "+disciplina.getProfessor().getNome()+" ministrará a disciplina: "+disciplina.getNome());
    		}
    			    	        
    		int cont = 1;
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
    	
    	//Seta os valores para a execução do algoritmo
    	Algoritmo.setDisciplinas(disciplinas);
    	
    	//Define a taxa de crossover do algoritmo
        Algoritmo.setTaxaDeCrossover(Dados.getTaxaDeCrossover());
        
        //Define a taxa de mutação do algoritmo
        Algoritmo.setTaxaDeMutacao(Dados.getTaxaDeMutacao());
        
        //Eltismo: Manutenção do melhor indivíduo na próxima geração
        boolean eltismo = true;
        
        //Tamanho da população que será criada
        int tamanhoPopulacao = Dados.getTamanhoMaximoPopulacao();
        
        //Número máximo de gerações do algoritmo
        int numMaxGeracoes = Dados.getNumeroMaximoGeracoes();
        
        //Define o número de genes do indivíduo baseado na quantidadde de disciplinas que devem ser alocadas
        int numGenes = Algoritmo.getDisciplinas().size();
         
        //Cria a primeira população aleatoriamente
        Populacao populacao = new Populacao(numGenes, tamanhoPopulacao);
        
        // Avalia a primeira população gerada, verificando se a solução procurada foi encontrada
        boolean temSolucao = populacao.avaliarPopulacao(numGenes);   
        int geracao = 0;
                
        System.out.println("Iniciando... Aptidão da solução: "+numGenes);
        
        int aptidaoIndividuoPopulacaoAnterior = 0;
        
        //Enquanto a solução não for encontrada e a quantidade de gerações não atingir o número máximo de gerações
        while (!temSolucao && geracao < numMaxGeracoes) {
            geracao++;
            
            //Cria uma nova população
            populacao = Algoritmo.novaGeracao(populacao, eltismo);

            int aux = populacao.getMelhorIndividuo().getAptidao();
            
            if(aux > aptidaoIndividuoPopulacaoAnterior) {
            	//Imprime o melhor indivíduo da população
                System.out.println("Geraçãoo " + geracao + " | Aptidão: " + populacao.getMelhorIndividuo().getAptidao());
                
            	aptidaoIndividuoPopulacaoAnterior = aux;
            }
            
            //Avalia a nova geração criada, verificando se a solução foi encontrada 
            temSolucao = populacao.getMelhorIndividuo().getAptidao() == numGenes; 
            
        }
        
        if(temSolucao) {
        	System.out.println("\n\nRESULTADO OBTIDO ATRAVÉS DA SOLUÇÃO COM "+geracao+" GERAÇÕES!");
        } if(geracao == numMaxGeracoes) {
        	System.out.println("\n\nRESULTADO OBTIDO ATRAVÉS DO NÚMERO MÁXIMO DE GERAÇÕES!");
        }
        
        
        return populacao.getMelhorIndividuo().getGenes();

    }
}