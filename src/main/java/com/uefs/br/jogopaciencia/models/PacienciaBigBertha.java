package com.uefs.br.jogopaciencia.models;

import com.uefs.br.jogopaciencia.gui.Pilha2D;

public class PacienciaBigBertha extends JogoStrategy {

	public static int ESTOQUE = 1;

	public static int FUNDACAO_1 = 2;
	public static int FUNDACAO_8 = 9;
	
	public static int FUNDACAO_9_REIS = 10;

	public static int FILEIRA_1 = 11;
	public static int FILEIRA_14 = 25;

	public static int QTD_PILHAS = 25;



	@Override
	public void novoJogo() throws Exception {
		this.pilhas = new Pilha[QTD_PILHAS];

		Baralho baralho = new Baralho(true);

		criarEstoque(baralho);
		criarFundacoes();
		criarFundacoesSoReis();
		criarFileiras(baralho);

	}

	private void criarEstoque(Baralho baralho) throws Exception {
		Pilha pilhaEstoque = new Pilha(ESTOQUE, "ESTOQUE");


		for(int i = 0; i < 14; i++)
			pilhaEstoque.adicionarCarta(baralho.getCarta());

		pilhaEstoque.setRegraAdicao((Carta cartaAnterior, Carta novaCarta) -> {return false;});

		pilhas[converterParaIndiceArray(ESTOQUE)] = pilhaEstoque;
	}

	private void criarFundacoes() {
		for(int i = 0; i < 8; i++) {
			Pilha pilhaFundacao = new Pilha(FUNDACAO_1 + i, "FUNDACAO");

			pilhaFundacao.setRegraAdicao((Carta cartaAnterior, Carta novaCarta) -> {
				if(cartaAnterior != null) {
					if(!cartaAnterior.getNaipe().equals(novaCarta.getNaipe()))
						return false;

					if( novaCarta.getNumero() - cartaAnterior.getNumero() != 1)
						return false;
				}
				else {
					return novaCarta.eAz();
				}

				return true;
			});
			
			pilhas[converterParaIndiceArray(FUNDACAO_1) + i] = pilhaFundacao;
		}

	}

	private void criarFundacoesSoReis() {
		Pilha pilhaFundacaoSoReis = new Pilha(FUNDACAO_9_REIS, "FUNDACAO (SO REIS)");
		
		pilhaFundacaoSoReis.setRegraAdicao((Carta cartaAnterior, Carta novaCarta) -> {
			return novaCarta.eReis();
		});
		
		pilhas[converterParaIndiceArray(FUNDACAO_9_REIS)] = pilhaFundacaoSoReis;
		

	}

	private void criarFileiras(Baralho baralho) throws Exception {
		for(int i = 0; i < 15; i++) {
			Pilha pilhaFileira = new Pilha(FILEIRA_1 + i, "FILEIRA");

			for(int j = 0; j < 6; j++)
				pilhaFileira.adicionarCarta(baralho.getCarta());

			pilhaFileira.setRegraAdicao((Carta cartaAnterior, Carta novaCarta) -> {
				if(cartaAnterior != null) {
					if(cartaAnterior.getCor().equals(novaCarta.getCor()))
						return false;

					if(cartaAnterior.getNumero() - novaCarta.getNumero() != 1)
						return false;
					
					
				}

				return true;
			});

			pilhas[converterParaIndiceArray(FILEIRA_1) + i] = pilhaFileira;

		}

	}

	@Override
	public void moverCarta(int numPilhaOrigem, int quantidade, int numPilhaDestino) throws Exception{
		if(numPilhaOrigem >= FUNDACAO_1 && numPilhaOrigem <= FUNDACAO_9_REIS)
			throw new Exception("Nao e' permitido retornar a carta do topo das fundacoes para as fileiras");

		super.moverCarta(numPilhaOrigem, quantidade, numPilhaDestino);
	}
	
	@Override
	public void moverCarta(int numPilhaOrigem, int numPilhaDestino) throws Exception{
		if(numPilhaOrigem >= FUNDACAO_1 && numPilhaOrigem <= FUNDACAO_9_REIS)
			throw new Exception("Nao e' permitido retornar a carta do topo das fundacoes para as fileiras");

		super.moverCarta(numPilhaOrigem, numPilhaDestino);
	}


	@Override
	protected int converterParaIndiceArray(int indiceContante) {
		return indiceContante - 1;
	}

	@Override
	public boolean terminou() {
		int quantidadeCartasNasFundacoes = 0;
		
		for(int pilha = FUNDACAO_1; pilha <= FUNDACAO_9_REIS; pilha++){
			quantidadeCartasNasFundacoes += pilhas[converterParaIndiceArray(pilha)].tamanho();
		}

		return quantidadeCartasNasFundacoes == 104;
	}

}
