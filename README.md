# calculator

A calculator with a support for functions (and fractions - thanks to Clojure).


## Setup

[Clojure](https://clojure.org/) and [Leiningen](https://leiningen.org/) have to be installed and configured.

Run tests:
```
lein test
```

Run the calculator:
```
lein run
```

Run as a standalone JAR:
```
lein uberjar
java -jar target/uberjar/calculator-_VERSION_-standalone.jar
```


## Usage

When you run the calculator, you may type in an mathematical expression you want to evaluate, eg. `((8.0 - log(2, 16))^3! + 2*52) /100`, or a command.

### Available commands
- `help`, `h` or `?`: Show help
- `exit`, `quit` or `q`: Exit the calculator

### Supported features

#### operators
- `+`
- `-` (both subtraction and unary negation)
- `*`
- `/`
- `^` (power)
- `!` (factorial)

#### functions
- *n*th root
  - `sqrt(x)`
  - `cbrt(x)`
- logarithms
  - `log(base, x)`
  - `ln(x)`
  - `log10(x)`
  - `log2(x)` or `lb(x)`
- goniometric
  - `sin(x)`
  - `cos(x)`
  - `tan(x)` or `tg(x)`
  - `cot(x)` or `cotg(x)`
- rounding
  - `round(x)`
  - `floor(x)`
  - `ceil(x)`
- other
  - `abs(x)`
  - `sgn(x)` ([signum function](https://en.wikipedia.org/wiki/Sign_function))
  - `double(x)`

#### contants
- `pi`
- `e`
- `phi` or `golden_ratio`

#### special characters
- parentheses: `(...)` or `[...]`
- separator (between function arguments): `,` or `;`
