package personal.shobhit.dsa.trees;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

/**
 * Binary Search Tree (BST) implementation with basic operations such as insertion, deletion,
 * finding, and more. This implementation allows duplicate values by inserting them to the left
 * subtree.
 */
@SuppressWarnings({"SameParameterValue", "ReturnOfNull", "java:S106"})
@Builder
class Bst {
  private TreeNode node;

  static void main() {
    final var tree = TreeNode.builder().value(null).build();
    final var tree2 = TreeNode.builder().value(null).build();

    insertAll(tree, 5, 3, 7, 2, 4, 6, 5);
    insertAll(tree2, 0, 1, 2, 8, 9);

    merge(tree, tree2);

    System.out.println("Original tree:");
    TreeNode.print(tree);

    deleteAll(tree, 7, 2);

    System.out.println("pop output:" + pop(tree).value());

    System.out.println("\nfind 3: " + find(tree, 3));

    replace(tree, 4, 10);

    System.out.println("\nlca of 6 and 10: " + lca(tree, 6, 10));
    System.out.println("lca of 5 and 10: " + lca(tree, 5, 10));

    System.out.println("\n4th smallest: " + findKthSmallest(tree, 4));

    System.out.println("\nSorted List: " + bstToSortedList(tree));

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

    if (node.getValue() == null) {
      return lca(node.getLeft(), value1, value2);
    }

    if (value1 <= node.getValue() && value2 <= node.getValue()) {
      return lca(node.getLeft(), value1, value2);
    } else if (value1 > node.getValue() && value2 > node.getValue()) {
      return lca(node.getRight(), value1, value2);
    }

    return node;
  }

  private static void replace(
      @NonNull final TreeNode root, final Integer oldValue, final Integer newValue) {
    delete(root, oldValue);
    insert(root, TreeNode.builder().value(newValue).build());
  }

  private static TreeNode find(final TreeNode node, final Integer value) {
    if (node == null) {
      return null;
    } else if (node.getValue() == null) {
      return find(node.getLeft(), value);
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
      return null;
    }

    if (node.getLeft() == null) {
      return PopOutput.builder().value(node.getValue()).node(node.getRight()).build();
    }

    final var popOutput = pop(node.getLeft());
    node.setLeft(popOutput.node());

    return PopOutput.builder().value(popOutput.value()).node(node).build();
  }

  private static void insertAll(@NonNull final TreeNode node, final Integer... values) {
    for (final var value : values) {
      insert(node, TreeNode.builder().value(value).build());
    }
  }

  private static void deleteAll(@NonNull final TreeNode node, final Integer... values) {
    for (final var value : values) {
      delete(node, value);
    }
  }

  private static TreeNode delete(final TreeNode node, final Integer value) {
    if (node == null) {
      return null;
    }
    if (node.getValue() == null) {
      node.setLeft(delete(node.getLeft(), value));
      return node;
    }
    if (value < node.getValue()) {
      node.setLeft(delete(node.getLeft(), value));
    } else if (value > node.getValue()) {
      node.setRight(delete(node.getRight(), value));
    } else {
      if (node.getLeft() == null || node.getRight() == null) {
        return node.getRight() != null ? node.getRight() : node.getLeft();
      }
      node.setValue(pop(node.getRight()).value());
    }
    return node;
  }

  private static void merge(@NonNull final TreeNode node1, @NonNull final TreeNode node2) {
    insert(node1, node2);
  }

  private static List<Integer> bstToSortedList(final TreeNode node) {
    if (node == null) {
      return new ArrayList<>();
    }
    if (node.getValue() == null) {
      return bstToSortedList(node.getLeft());
    }
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

  private static TreeNode insert(final TreeNode node, @NonNull final TreeNode value) {
    if (node == null) {
      return value;
    }

    if (value.getValue() == null) {
      return insert(node, value.getLeft());
    }

    if (node.getValue() == null) {
      node.setLeft(insert(node.getLeft(), value));
      return node;
    }

    if (value.getValue() <= node.getValue()) {
      node.setLeft(insert(node.getLeft(), value));
    } else {
      node.setRight(insert(node.getRight(), value));
    }

    return node;
  }

  @Builder
  private record PopOutput(Integer value, TreeNode node) {}
}
