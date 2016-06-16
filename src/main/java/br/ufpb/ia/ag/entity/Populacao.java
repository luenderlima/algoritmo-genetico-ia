package br.ufpb.ia.ag.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Populacao {

    private List<Individuo> individuos;

    //Cria uma população com indivíduos aleatórios
    public Populacao(int numGenes, int tamanhoPopulacao) { 
        individuos = new ArrayList<Individuo>();
        for (int i = 0; i < tamanhoPopulacao; i++) {
            individuos.add(new Individuo(numGenes));
        }
    }
    
  	//Cria uma população com indivíduos sem valor, que serão compostos posteriormente
    public Populacao() {
        individuos = new ArrayList<Individuo>();
    }
    
    //Coloca um indivíduo em uma certa posição da população
    public void setIndividuo(Individuo individuo, int posicao) {
        individuos.add(posicao, individuo);
    }

    //Adiciona um indivíduo na próxima posição disponível da população
    public void adicionarIndividuo(Individuo individuo) {
    	individuos.add(individuo);
    }

    /** ordena a população pelo valor de aptidão de cada indivíduo, do maior valor para o menor, assim se quiser obter o melhor indivíduo desta população 
     * basta acessar o indivíduo que está na primeira posição
     */
    public void ordenarPopulacao() {
    	Collections.sort(individuos);
    }
    
    /**
     * verifica se algum indivíduo da população possui a solução
     */
    public boolean avaliarPopulacao(int aptidaoMaxima) {
    	for(Individuo individuo: this.individuos) {
    		if(individuo.getAptidao() == aptidaoMaxima) {
    			return true;
    		}
    	}
    	return false;
   
    }

    /**
     * Número de indivíduos existentes na população
     */
    public int getNumIndividuos() {
        int num = 0;
        for (int i = 0; i < individuos.size(); i++) {
            if (individuos.get(i) != null) {
                num++;
            }
        }
        return num;
    }
    
    public Individuo getMelhorIndividuo() {
    	return this.individuos.get(0);
    }

    public int getTamPopulacao() {
        return individuos.size();
    }

    public Individuo getIndividuo(int pos) {
        return individuos.get(pos);
    }

	public List<Individuo> getIndividuos() {
		return individuos;
	}
    
}