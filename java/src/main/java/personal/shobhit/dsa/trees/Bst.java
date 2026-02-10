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
    final var tree = TreeNode.builder().value(5).build();
    final var tree2 = TreeNode.builder().value(0).build();

    insertAll(tree, 3, 7, 2, 4, 6, 5);
    insertAll(tree2, 1, 2, 8, 9);

    merge(tree, tree2);

    System.out.println("Original tree:");
    TreeNode.print(tree);

    deleteAll(tree, 7, 2);

    System.out.println("pop output:" + pop(tree));

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

  private static TreeNode lca(final TreeNode node, final int value1, final int value2) {
    return switch (node) {
      case null -> null;
      case final TreeNode n when value1 <= n.getValue() && value2 <= n.getValue() ->
          lca(n.getLeft(), value1, value2);
      case final TreeNode n when value1 > n.getValue() && value2 > n.getValue() ->
          lca(n.getRight(), value1, value2);
      default -> node;
    };
  }

  private static void replace(
      @NonNull final TreeNode root, final int oldValue, final int newValue) {
    delete(root, oldValue);
    insert(root, TreeNode.builder().value(newValue).build());
  }

  private static TreeNode find(final TreeNode node, final int value) {
    return switch (node) {
      case null -> null;
      case final TreeNode n when n.getValue() == value -> n;
      case final TreeNode n ->
          value < n.getValue() ? find(n.getLeft(), value) : find(n.getRight(), value);
    };
  }

  private static PopOutput pop(final TreeNode node) {
    return switch (node) {
      case null -> null;
      case final TreeNode n when n.getLeft() == null ->
          PopOutput.builder().value(n.getValue()).node(n.getRight()).build();
      default -> {
        final var popOutput = pop(node.getLeft());
        //noinspection DataFlowIssue
        node.setLeft(popOutput.node());
        yield PopOutput.builder().value(popOutput.value()).node(node).build();
      }
    };
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

  private static TreeNode delete(final TreeNode node, final int value) {
    return switch (node) {
      case null -> null;
      case final TreeNode n when value < n.getValue() -> {
        n.setLeft(delete(n.getLeft(), value));
        yield n;
      }
      case final TreeNode n when value > n.getValue() -> {
        n.setRight(delete(n.getRight(), value));
        yield n;
      }
      case final TreeNode n -> {
        if (n.getLeft() == null || n.getRight() == null) {
          yield n.getRight() != null ? n.getRight() : n.getLeft();
        } else {
          //noinspection DataFlowIssue
          yield TreeNode.builder()
              .value(pop(n.getLeft()).value())
              .left(n.getLeft())
              .right(n.getRight())
              .build();
        }
      }
    };
  }

  private static void merge(@NonNull final TreeNode node1, @NonNull final TreeNode node2) {
    insert(node1, node2);
  }

  private static List<Integer> bstToSortedList(final TreeNode node) {
    return node == null ? new ArrayList<>() : bstToSortedList(node, new ArrayList<>());
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
    return switch (node) {
      case null -> value;
      case final TreeNode n when value.getValue() <= n.getValue() -> {
        n.setLeft(insert(n.getLeft(), value));
        yield n;
      }
      case final TreeNode n -> {
        n.setRight(insert(n.getRight(), value));
        yield n;
      }
    };
  }

  @Builder
  private record PopOutput(int value, TreeNode node) {}
}
