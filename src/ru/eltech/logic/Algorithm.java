package ru.eltech.logic;

public interface Algorithm {
    /**
     * @param context Граф, над которым требуется выполнить алгоритм
     * @return Набор кадров, показывающий состояние графа в последовательные моменты времени исполнения алгоритма
     */
    FrameList process(Graph context);
}
