package br.ufpb.ia.ag.entity;


public class Populacao {

    private Individuo[] individuos;
    private int tamanhoPopulacao;

    //Cria uma população com indivíduos aleatórios
    public Populacao(int numGenes, int tamanhoPopulacao) { // tirar o numGenes
        this.tamanhoPopulacao = tamanhoPopulacao;
        individuos = new Individuo[tamanhoPopulacao];
        for (int i = 0; i < individuos.length; i++) {
            individuos[i] = new Individuo(numGenes);
        }
    }

    //Cria uma população com indivíduos sem valor, que serão compostos posteriormente
    public Populacao(int tamanhoPopulacao) {
        this.tamanhoPopulacao = tamanhoPopulacao;
        individuos = new Individuo[tamanhoPopulacao];
        for (int i = 0; i < individuos.length; i++) {
            individuos[i] = null;
        }
    }
    
    //Coloca um indivíduo em uma certa posição da população
    public void setIndividuo(Individuo individuo, int posicao) {
        individuos[posicao] = individuo;
    }

    //Adiciona um indivíduo na próxima posição disponível da população
    public void adicionarIndividuo(Individuo individuo) {
        for (int i = 0; i < individuos.length; i++) {
            if (individuos[i] == null) {
                individuos[i] = individuo;
                return;
            }
        }
    }

    /** ordena a população pelo valor de aptidão de cada indivíduo, do maior valor para o menor, assim se eu quiser obter o melhor indivíduo desta população, 
     * acesso a população 0 do array de indivíduos 
     */
    public void ordenarPopulacao() {
        boolean trocou = true;
        while (trocou) {
            trocou = false;
            for (int i = 0; i < individuos.length - 1; i++) {
                if (individuos[i].getAptidao() < individuos[i + 1].getAptidao()) {
                    Individuo temp = individuos[i];
                    individuos[i] = individuos[i + 1];
                    individuos[i + 1] = temp;
                    trocou = true;
                }
            }
        }
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
        for (int i = 0; i < individuos.length; i++) {
            if (individuos[i] != null) {
                num++;
            }
        }
        return num;
    }
    
    public Individuo getMelhorIndividuo() {
    	return this.individuos[0];
    }

    public int getTamPopulacao() {
        return tamanhoPopulacao;
    }

    public Individuo getIndividuo(int pos) {
        return individuos[pos];
    }
    
}