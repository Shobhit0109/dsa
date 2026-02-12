package personal.shobhit.dsa.trees;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@SuppressWarnings({
  "java:S106",
  "java:S3358",
  "NestedConditionalExpression",
  "MethodWithMoreThanThreeNegations",
  "CallToSimpleSetterFromWithinClass"
})
class TreeNode {
  private static final String GREEN = "\u001B[32m"; // left
  private static final String BLUE = "\u001B[34m"; // right
  private static final String RESET = "\u001B[0m";
  private final int value;
  @Builder.Default private int height = 1;
  private TreeNode left;
  private TreeNode right;

  static void print(final TreeNode node) {
    System.out.println("\nNormal print:");
    System.out.println(node);

    System.out.println("\nPretty print:");
    print(node, "", true, "");
  }

  private static void print(
      final TreeNode node, final String prefix, final boolean isTail, final CharSequence label) {
    if (node == null) return;

    final String connector = isTail ? "└── " : "├── ";
    final String color =
        "L".contentEquals(label) ? GREEN : ("R".contentEquals(label) ? BLUE : RESET);

    System.out.println(
        prefix
            + connector
            + color
            + label
            + (label.isEmpty() ? "" : ": ")
            + "v:"
            + node.getValue()
            + ",h:"
            + node.getHeight()
            + RESET);

    final TreeNode left = node.getLeft();
    final TreeNode right = node.getRight();

    final boolean hasLeft = left != null;
    final boolean hasRight = right != null;

    if (!hasLeft && !hasRight) {
      return;
    }

    // left child (not tail if right exists)
    if (hasLeft) {
      print(
          left,
          prefix + (isTail ? "    " : "│   "),
          !hasRight, // if right exists, left is NOT tail
          "L");
    }

    // right child (always tail)
    if (hasRight) {
      print(right, prefix + (isTail ? "    " : "│   "), true, "R");
    }
  }

  static void main() {
    // Create a sample tree
    final var tree = builder().value(5).build();

    tree.setLeft(builder().value(3).build());
    tree.setRight(builder().value(7).build());
    tree.getLeft().setLeft(builder().value(2).build());
    tree.getLeft().getLeft().setRight(builder().value(1).build());
    tree.getLeft().setRight(builder().value(4).build());
    tree.getRight().setLeft(builder().value(6).build());
    tree.getRight().setRight(builder().value(8).build());

    // Print the tree
    print(tree);
  }
}
