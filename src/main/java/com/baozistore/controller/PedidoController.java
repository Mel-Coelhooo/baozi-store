package com.baozistore.controller;

import com.baozistore.model.Cliente;
import com.baozistore.model.Pedido;
import com.baozistore.model.Produto;
import com.baozistore.repository.ClienteRepository;
import com.baozistore.repository.PedidoRepository;
import com.baozistore.repository.ProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoController(PedidoRepository pedidoRepository,
                            ClienteRepository clienteRepository,
                            ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    /**
     * POST /pedidos - criar
     * Corpo esperado: { "clienteId": 1, "produtoId": 1, "quantidade": 10 }
     */
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody PedidoRequest req) {
        Pedido pedido = new Pedido();
        ResponseEntity<?> erro = preencher(pedido, req);
        if (erro != null) {
            return erro;
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoRepository.save(pedido));
    }

    // GET /pedidos - listar todos
    @GetMapping
    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    // GET /pedidos/{id} - consultar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return pedidoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /pedidos/{id} - atualizar (opcional)
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody PedidoRequest req) {
        Optional<Pedido> existente = pedidoRepository.findById(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Pedido pedido = existente.get();
        ResponseEntity<?> erro = preencher(pedido, req);
        if (erro != null) {
            return erro;
        }
        return ResponseEntity.ok(pedidoRepository.save(pedido));
    }

    // DELETE /pedidos/{id} - apagar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        if (!pedidoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        pedidoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /** Valida os dados recebidos e preenche o pedido. Retorna um erro 400, ou null se estiver tudo certo. */
    private ResponseEntity<?> preencher(Pedido pedido, PedidoRequest req) {
        if (req.clienteId() == null || req.produtoId() == null || req.quantidade() == null) {
            return badRequest("Informe clienteId, produtoId e quantidade.");
        }
        if (req.quantidade() <= 0) {
            return badRequest("A quantidade deve ser maior que zero.");
        }
        Optional<Cliente> cliente = clienteRepository.findById(req.clienteId());
        if (cliente.isEmpty()) {
            return badRequest("Cliente " + req.clienteId() + " não encontrado.");
        }
        Optional<Produto> produto = produtoRepository.findById(req.produtoId());
        if (produto.isEmpty()) {
            return badRequest("Produto " + req.produtoId() + " não encontrado.");
        }
        pedido.setCliente(cliente.get());
        pedido.setProduto(produto.get());
        pedido.setQuantidade(req.quantidade());
        return null;
    }

    private ResponseEntity<Map<String, String>> badRequest(String mensagem) {
        return ResponseEntity.badRequest().body(Map.of("erro", mensagem));
    }

    /** Dados recebidos no corpo da requisição de pedido. */
    public record PedidoRequest(Long clienteId, Long produtoId, Integer quantidade) {
    }
}
