package personal.shobhit.dsa.trees;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;

/**
 * Adelson-Velsky and Landis Tree (AVL) implementation with basic operations such as insertion,
 * deletion, finding, and more. This implementation allows duplicate values by inserting them to the
 * left subtree.
 */
@SuppressWarnings({"SameParameterValue", "ReturnOfNull", "java:S106"})
@Builder
class Avl {
  private TreeNode node;

  static void main() {
    TreeNode tree = null;

    tree = insertAll(tree, 5, 3, 7, 2, 4, 6, 5, 1);
    tree = merge(tree, insertAll(null, 8, 9));

    tree = deleteAll(tree, 7);

    final var popOutput = pop(tree);
    System.out.println("pop output:" + popOutput.value());
    tree = popOutput.node();

    System.out.println("\nfind 3: " + find(tree, 3));

    tree = replace(tree, 4, 10);

    System.out.println("\nlca of 6 and 10: " + lca(tree, 6, 10));
    System.out.println("lca of 5 and 10: " + lca(tree, 5, 10));

    System.out.println("\n4th smallest: " + findKthSmallest(tree, 4));

    TreeNode.print(tree);
  }

  private static Integer findKthSmallest(final TreeNode node, final int k) {
    final var sortedSet = bstToSortedList(node).stream().distinct().toList();
    return (k > 0 && k <= sortedSet.size()) ? sortedSet.get(k - 1) : null;
  }

  private static TreeNode lca(final TreeNode node, final Integer value1, final Integer value2) {
    if (node == null) {
      return null;
    }

    if (value1 <= node.getValue() && value2 <= node.getValue()) {
      return lca(node.getLeft(), value1, value2);
    } else if (value1 > node.getValue() && value2 > node.getValue()) {
      return lca(node.getRight(), value1, value2);
    }

    return node;
  }

  private static TreeNode replace(
      final TreeNode node, final Integer oldValue, final Integer newValue) {
    return insert(delete(node, oldValue), newValue);
  }

  private static TreeNode find(final TreeNode node, final Integer value) {
    if (node == null) {
      return null;
    } else if (node.getValue().equals(value)) {
      return node;
    } else if (value < node.getValue()) {
      return find(node.getLeft(), value);
    } else {
      return find(node.getRight(), value);
    }
  }

  private static PopOutput pop(final TreeNode node) {
    if (node == null) {
      return PopOutput.builder().value(null).node(null).build();
    }

    if (node.getLeft() == null) {
      return PopOutput.builder().value(node.getValue()).node(node.getRight()).build();
    }

    final var popOutput = pop(node.getLeft());
    node.setLeft(popOutput.node());

    return PopOutput.builder().value(popOutput.value()).node(node).build();
  }

  private static TreeNode insertAll(final TreeNode node, final Integer... values) {
    TreeNode current = node;
    for (final var value : values) {
      current = insert(current, value);
    }
    return current;
  }

  private static TreeNode deleteAll(final TreeNode node, final Integer... values) {
    TreeNode current = node;
    for (final var value : values) {
      current = delete(current, value);
    }
    return current;
  }

  private static TreeNode delete(final TreeNode node, final Integer value) {
    if (node == null) {
      return null;
    }
    var result = node.shallowCopy();
    if (value < node.getValue()) {
      result.setLeft(delete(node.getLeft(), value));
    } else if (value > node.getValue()) {
      result.setRight(delete(node.getRight(), value));
    } else {
      result = merge(node.getLeft(), node.getRight());
    }
    return result;
  }

  private static TreeNode merge(final TreeNode node1, final TreeNode node2) {
    if (node1 == null) return node2;
    if (node2 == null) return node1;

    final var sortedList1 = bstToSortedList(node1);
    final var sortedList2 = bstToSortedList(node2);

    return mergeSortedListsToBst(sortedList1, sortedList2);
  }

  @SafeVarargs
  private static TreeNode mergeSortedListsToBst(final List<Integer>... lists) {
    // todo: merge sorted lists in O(n) time instead of O(n log n) time
    final List<Integer> mergedList = new ArrayList<>();
    for (final var list : lists) {
      mergedList.addAll(list);
    }
    return insertAll(null, mergedList.toArray(Integer[]::new));
  }

  private static List<Integer> bstToSortedList(final TreeNode node) {
    return bstToSortedList(node, new ArrayList<>());
  }

  private static List<Integer> bstToSortedList(final TreeNode node, final List<Integer> result) {
    if (node != null) {
      bstToSortedList(node.getLeft(), result);
      result.add(node.getValue());
      bstToSortedList(node.getRight(), result);
    }
    return result;
  }

  private static TreeNode insert(final TreeNode node, final Integer value) {
    if (node == null) {
      return TreeNode.builder().value(value).build();
    }
    final var result = node.shallowCopy();
    if (node.getValue().equals(value)) {
      final var newNode = TreeNode.builder().value(value).left(node.getLeft()).build();
      result.setLeft(newNode);
    } else if (value < node.getValue()) {
      result.setLeft(insert(node.getLeft(), value));
    } else {
      result.setRight(insert(node.getRight(), value));
    }
    return result;
  }

  @Builder
  private record PopOutput(Integer value, TreeNode node) {}
}
