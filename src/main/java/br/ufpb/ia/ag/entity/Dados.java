package br.ufpb.ia.ag.entity;

public class Dados {
	
	private static final int QUANTIDADE_DIAS = 5;
	private final static double TAXA_DE_MUTACAO  = 0.03;
	private final static double TAXA_DE_CROSSOVER = 0.6;
	private final static int TAMANHO_MAXIMO_POPULACAO = 100;
	private final static int NUMERO_MAXIMO_GERACOES = 5000;
	
	private static final String[] HORARIOS_DE_AULA = {"M2", "M3", "M4", "M5", "T1", "T2", "T3", "T4"};
	private static final String[] DIAS_DA_SEMANA = {"Segunda-Feira", "Terça-Feira", "Quarta-Feira", "Quinta-Feira", "Sexta-Feira"};

    public static int getQuantidadeDias() {
		return QUANTIDADE_DIAS;
	}
    
	public static double getTaxaDeMutacao() {
		return TAXA_DE_MUTACAO;
	}

	public static double getTaxaDeCrossover() {
		return TAXA_DE_CROSSOVER;
	}
	
	public static int getTamanhoMaximoPopulacao() {
		return TAMANHO_MAXIMO_POPULACAO;
	}

	public static int getNumeroMaximoGeracoes() {
		return NUMERO_MAXIMO_GERACOES;
	}
	
	public static String getDiaDaSemana(int indiceDia) {
		return DIAS_DA_SEMANA[indiceDia];
	}
	
	public static String getHorarioDeAula(int indiceHorario) {
		return HORARIOS_DE_AULA[indiceHorario];
	}
	
	public static int getHorarioMaximo() {
		return HORARIOS_DE_AULA.length;
	}
	
	public static int getIndiceDiaDaSemana(String diaDaSemana) {
		for(int i = 0; i < DIAS_DA_SEMANA.length; i++) {
			if(DIAS_DA_SEMANA[i].toUpperCase().equals(diaDaSemana.toUpperCase())) {
				return i;
			}
		}
		return -1;
	}
	
}
