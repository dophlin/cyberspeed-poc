# Scratch Game Proof of Concept - Project Documentation

## Overview

This repository contains the implementation for a Proof of Concept (PoC) assignment focusing on a scratch game simulation. The project has been meticulously crafted to demonstrate a structured approach to game logic development, ensuring the core functionalities align with the specified requirements. For detailed assignment criteria, please consult `assignment.md` included in this repository.

## Solution Architecture

The solution is engineered to encompass the following key components, collectively forming the backbone of the scratch game simulation:

![Image description](./diagram.svg)


1. **Configuration Extraction**: Initialization begins with the extraction of game configuration parameters and the betting amount, derived from the input arguments.

2. **Matrix Generation**: A core matrix structure is generated, where each cell is populated with standard symbols. The distribution of these symbols adheres strictly to the probabilities defined in the input configuration.

3. **Bonus Symbol Allocation**: The game dynamically selects a random bonus symbol, governed by the input configuration's probability metrics. This symbol is then randomly placed within a cell in the matrix.

4. **Winning Combination Analysis**: The algorithm calculates the winning combinations for each symbol, effectively mapping out potential winning scenarios.

5. **Bonus Combination Valuation**: In addition to standard combinations, the system evaluates and values the bonus winning combinations.

6. **Final Reward Computation**: The main purpose of the game logic is the calculation of the final reward. This computation takes into account the winning combinations of each standard symbol along with the bonus symbol's reward type, offering the final reward value.

7. **Output Response Generation**: Finally, the game compiles an output response encapsulating the game results. This response is then displayed on the output console, providing a clear and concise summary of the game outcome.

## Additional Notes

- The code is thoroughly commented to aid in understanding the flow and functionality.
- Two test scenarios are provided in the project: One Win scenario and one Loss scenario.
- This PoC serves as a foundational blueprint and can be extended or modified to suit diverse gaming scenarios.
- For some reason, the provided 'reward' value in some samples of the 'assignment.md' may not be correct, but the PoC calculates the reward values mathematically correct.
- The 'reward' value is calculated in the double format but for the print output, it is cast to int to be matched with the 'assignment.md' document.
- - The value of 'correct_area' in the 'same_symbols_horizontally' winning combination in the original assignment.md was not probably correct, so I fixed it:
  - Previous value:
    ```javascript
       "same_symbols_horizontally": { // OPTIONAL
            "reward_multiplier": 2,
            "when": "linear_symbols",
            "group": "horizontally_linear_symbols",
            "covered_areas": [
                ["0:0", "0:1", "0:2"],
                ["1:0", "1:1", "1:1"], // the third column value is "1:1"
                ["2:0", "2:1", "2:2"]
            ]
        }
    ```
  - Fixed value:
    ```javascript
       "same_symbols_horizontally": { // OPTIONAL
            "reward_multiplier": 2,
            "when": "linear_symbols",
            "group": "horizontally_linear_symbols",
            "covered_areas": [
                ["0:0", "0:1", "0:2"],
                ["1:0", "1:1", "1:2"], // the third column value has changed to "1:2"
                ["2:0", "2:1", "2:2"]
            ]
        }
    ```
