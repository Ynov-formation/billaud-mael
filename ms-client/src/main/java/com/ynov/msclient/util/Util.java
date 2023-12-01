package com.ynov.msclient.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.Set;

public class Util {
  /**
   * Copie les propriétés non nulles d'un objet source vers un objet cible
   *
   * @param source objet dont on veut copier les propriétés non nulles
   * @param target objet dans lequel on veut copier les propriétés non nulles
   */
  public static void copyNonNullProperties(Object source, Object target) {
    BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
  }

  /**
   * Retourne les noms des propriétés nulles d'un objet
   *
   * @param source objet dont on veut récupérer les noms des propriétés nulles
   *
   * @return tableau de String contenant les noms des propriétés nulles
   */
  public static String[] getNullPropertyNames(Object source) {
    final BeanWrapper src = new BeanWrapperImpl(source);
    java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

    Set<String> emptyNames = new HashSet<>();
    for (java.beans.PropertyDescriptor pd : pds) {
      Object srcValue = src.getPropertyValue(pd.getName());
      if (srcValue == null) {
        emptyNames.add(pd.getName());
      }
    }
    String[] result = new String[emptyNames.size()];
    return emptyNames.toArray(result);
  }
}
