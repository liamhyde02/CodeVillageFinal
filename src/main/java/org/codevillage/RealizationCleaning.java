package org.codevillage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;

public class RealizationCleaning extends CleaningChainLink{
    public RealizationCleaning(CleaningChain next) {
        super(next);
    }
    public void clean(JavaEntity entity, ArrayList<String> classNames) {
        JavaClass javaClass = (JavaClass) entity;
        javaClass.getRealizations().removeIf(realization -> !classNames.contains(realization));
        next.clean(entity, classNames);
    }
}
