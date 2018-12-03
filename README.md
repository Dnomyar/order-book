# Order book implementation
The project simulates simplified model of an order book on a financial exchange.

The constraint is not to use libraries other than what is necessary for the tests.

## Architecture
This project following the hexagonal architecture :
- the package `application` contains code to interact with the application. In that case, the interaction with the console. 
- the package `domain` contains the business logic and the business objects.


## Efficiency of operations
- `N`ew order in the book : `O(log(book_depth))`
- `U`pdate an order in the book : `O(log(book_depth))`
- `D`elete an order in the book : `O(log(book_depth))`

Overall, the complexity of the algorithm is `O(n * log(book_depth))` where `n` is the number of operations.


### Data structure: immutable indexed AVL Tree
#### Description
To have this this complexity, the data structure used is an immutable AVL tree (self-balanced tree). The tree is transformed to be able to have this interface : 
```scala
sealed trait AVLIndexedTree[+T] {
  def insert[U >: T](index: Int, elementToInsert: U): AVLIndexedTree[U]
  def delete[U >: T](index: Int): AVLIndexedTree[U]
  def updated[U >: T](index: Int, elementToInsert: U): AVLIndexedTree[U]
}
```
This interface allows to insert, delete and update nodes of the tree using indexes instead of values. 

#### How is works ?
Every node know the number of children in the left and the right side. This enable to know the which side to choose for a given index.

## Usage

### Run
```
sbt "run lot-of-updates.txt 10.0 100"
```
- `filename`: the name of the file containing the input data. The file contains instructions to update the order book. Line example : `N B 1 5 30`
    - Instruction: `N`ew, `U`pdate or `D`elete.
    - Side: `B`id or `A`sk.
    - Price Level Index: Price Level Index of change in range `1..book_depth`.
    - Price: Price in ticks.
    - Quantity: Number of contracts at price level.
- `tick size`: a floating point number giving the minimum $ price movements.
- `book depth`: an integer giving the number of price levels to keep track of.

#### Output
```
Bid Price[1], Bid Quantity[1], Ask Price[1], Ask Quantity[1] 
... 
Bid Price[n], Bid Quantity[n], Ask Price[n], Ask Quantity[n]
```
Where n is the book depth and price is in $.


### Test
```
sbt test
```



## Example
- `lot-of-updates.txt`
```
N B 1 5 30
N B 2 4 40
N A 1 6 10
N A 2 7 10
U A 2 7 20
U B 1 5 40
```

```
sbt "run lot-of-updates.txt 10.0 2"
```
- Output
```
50.0,40,60.0,10
40.0,40,70.0,20
```
