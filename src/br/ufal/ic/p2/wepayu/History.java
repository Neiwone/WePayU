package br.ufal.ic.p2.wepayu;

import java.util.Stack;

public class History {
    private final Stack<Memento> undoStack = new Stack<>();
    private final Stack<Memento> redoStack = new Stack<>();

    public void push(Memento m) {
        undoStack.push(m);
        redoStack.clear();
    }

    public Memento undo() throws Exception {
        if (undoStack.size() < 2)
            throw new Exception("Nao ha comando a desfazer.");
        redoStack.push(undoStack.pop());
        return undoStack.peek();
    }

    public Memento redo() throws Exception {
        if (redoStack.isEmpty())
            throw new Exception("Nao ha comando a refazer.");
        undoStack.push(redoStack.pop());
        return undoStack.peek();
    }

    public void size() {
        System.out.println("tamanho undoStack: " + undoStack.size());
        System.out.println("tamanho redoStack: " + redoStack.size());
    }


}
