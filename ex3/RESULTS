superman

loislane batman joker


=============================
=      File description     =
=============================
Game.java - contains the game implementation and the main method of the program
Tool.java - implements the Tool class.



=============================
=          Design           =
=============================
I implemented the Tool class in a way that it can be reused between games, meaning that instead of creating a new tool
 every game, I use the previously used tool and just reset it with new parameters.
I chose this design because it is similar to how tools act like in the real world:
different players use the same tool after they erased the name of the previous player from it.



=============================
=  Implementation details   =
=============================
In the Game class, in order to store the different players, I chose the TreeSet dataset because we perform many
 insertion and deletion actions on this dataset, actions which a balanced tree performs efficiently.


=============================
=    Answers to questions   =
=============================

1)
The smart player picks his moves by simply looking at all the possible valid moves, and picking the 
one that leaves the opponent with the minimal number of possible steps (and which does not make him lose)

2)
On the given example, the run time of the TresSet took 0.3445 seconds, While the run time of the HashSet took 0.56547.