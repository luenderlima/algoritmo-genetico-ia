package br.ufpb.ia.ag.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.ufpb.ia.ag.algorithm.Algoritmo;


public class Populacao {

    private List<Individuo> individuos;
    private int tamanhoPopulacao;

    //Cria uma popula��o com indiv�duos aleat�rios
    public Populacao(int numGenes, int tamanhoPopulacao) { 
        this.tamanhoPopulacao = tamanhoPopulacao;
        individuos = new ArrayList<Individuo>();
        for (int i = 0; i < tamanhoPopulacao; i++) {
            individuos.add(new Individuo(numGenes));
        }
    }
    /*
    //Cria uma popula��o com indiv�duos sem valor, que ser�o compostos posteriormente
    public Populacao(int tamanhoPopulacao) {
        this.tamanhoPopulacao = tamanhoPopulacao;
        individuos = new ArrayList<Individuo>();
        for (int i = 0; i < tamanhoPopulacao; i++) {
            individuos.add(null); //new Individuo(Algoritmo.getDisciplinas().size())
        }
    }
    */
    
  //Cria uma popula��o com indiv�duos sem valor, que ser�o compostos posteriormente
    public Populacao() {
        this.tamanhoPopulacao = 0;
        individuos = new ArrayList<Individuo>();
    }
    
    //Coloca um indiv�duo em uma certa posi��o da popula��o
    public void setIndividuo(Individuo individuo, int posicao) {
        individuos.add(posicao, individuo);
    }

    //Adiciona um indiv�duo na pr�xima posi��o dispon�vel da popula��o
    public void adicionarIndividuo(Individuo individuo) {
    	/*
        for (int i = 0; i < individuos.size(); i++) {
            if (individuos.get(i) == null) {
                individuos.add(i, individuo);
                return;
            }
        }
        */
    	individuos.add(individuo);
    }

    /** ordena a popula��o pelo valor de aptid�o de cada indiv�duo, do maior valor para o menor, assim se eu quiser obter o melhor indiv�duo desta popula��o, 
     * acesso a popula��o 0 do array de indiv�duos 
     */
    public void ordenarPopulacao() {
    	/*
        boolean trocou = true;
        while (trocou) {
            trocou = false;
            for (int i = 0; i < individuos.length - 1; i++) {
                if (individuos[i].getAptidao() < individuos[i + 1].getAptidao()) {
                    Individuo aux = individuos[i];
                    individuos[i] = individuos[i + 1];
                    individuos[i + 1] = aux;
                    trocou = true;
                }
            }
        }
        */
    	Collections.sort(individuos);
    }
    
    /**
     * verifica se algum indiv�duo da popula��o possui a solu��o
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
     * N�mero de indiv�duos existentes na popula��o
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
    
}