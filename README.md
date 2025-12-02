# BYOL
## About Language: 

<p><em>Language Name:</em> Mov</p>

<p><em>Filename Extension:</em> .mov</p>

<p><em>Description:</em> Mov is a specified-purpose programming language that has the purpose of access and interpreting information about movies and all that encompasses them. It's syntax is designed to be straightforward and easy to read for the user. Allowing the user the ability to access a movie data base of movie reviews and information to complete certain task. This language is ideal for movie reviewers and watchers that want a way to sort through movies and access the information that comes with them.</p>

<p>Link To Database: https://www.kaggle.com/datasets/rajugc/imdb-movies-dataset-based-on-genre?resource=download </p>

## Grammar: 

```
findStmt -> "find" kind query (str | identifier) (where | without. <condition>)

haveStmt -> "have" ... = <findStmt>

sayStmt -> "say" str (rating | summary) (num | str)

writeStmt -> "write" kind query str (where | without. <condition>)

kind -> "movies" | "ratings" | "genre" | "stars" | "year" | "summary" | "length" | "director" | age-appropriate

query -> "starring" | "directed by" | "for" | "in" | "of" | "is"

condition -> str | kind query str | <conditon> > <condition>
```

## Examples: 

### Example Program 1 Getter

```
find movies starring "Tom Hanks"
find movies directed by "Ryan Coogler"
find ratings for "Sing 2"
find genre for "Tom Hanks"
find genre of "Suicide 2"
find stars in "Black Panther: Wakanda Forever"
find summary of "Sing 2"
find length of "The Little Mermaid"
find year in "Black Panther: Wakanda Forever"
find year for "Tori Kelly"
find year of "Ryan Coogler"
find length of "Black Panther: Wakanda Forever"
find director for "Black Panther: Wakanda Forever"
find director of "Tori Kelly"
```

<p>Outputs:</p>

```
nothing just used to stores the values
```

### Example Program 2 Declaration

```
have actors = find stars in "Sing 2"
find movies starring actors
have tomMovies = find movies where "tom hanks"
 without "tom hanks"
have lkActors = find stars in "The Lion King"
find movies starring lkActors without "The Lion King"
```

<p>Outputs:</p>

```
| No. | Result              |
+-----+---------------------+
| 1   | Matthew McConaughey |
| 2   | Tori Kelly          |
| 3   | Reese Witherspoon   |
| 4   | Scarlett Johansson  |
| No. | Result                                       |
+-----+----------------------------------------------+
| 1   | Interstellar                                 |
| 2   | The Dark Tower                               |
| 3   | Legally Blonde                               |
| 4   | Girl with a Pearl Earring                    |
| 5   | Rendition                                    |
| 6   | Reign of Fire                                |
| 7   | Serenity                                     |
| 8   | Best Laid Plans                              |
| 9   | Asteroid City                                |
......
```

### Example Program 3 Setter

```
find ratings for "Sing 2"
say "Sing 2" ratings 9.5
find ratings for "Sing 2"
```

<p>Outputs:</p>

```
| No. | Result |
+-----+--------+
| 1   | 7.4    |
| No. | Result |
+-----+--------+
| 1   | 9.5    |
```

```
find summary of "Sing 2"
say "Sing 2" summary "Hello"
find summary of "Sing 2"
```

<p>Outputs:</p>

```
| No. | Result                                                                                                                  |
+-----+-------------------------------------------------------------------------------------------------------------------------+
| 1   | Buster Moon and his friends must persuade reclusive rock star Clay Calloway to join them for the opening of a new show. |
| No. | Result |
+-----+--------+
| 1   | Hello  |
```

### Example Program 4 Return/Print Output

```
write movies directed by "Ryan Coogler"
write stars in "The Lion King" where movie is "The Little Mermaid"
```

<p>Outputs:</p>

```
| No. | Result                         |
+-----+--------------------------------+
| 1   | Creed                          |
| 2   | Wrong Answer                   |
| 3   | Fruitvale Station              |
| 4   | Black Panther                  |
| 5   | Black Panther: Wakanda Forever |
```

### Example Program 5 Conditionals

```
find movies starring "Tom Hanks" where genre is "action"
find stars in "The Lion King" where movies starring "Tom Hanks"
```

<p>Outputs:</p>

```
returns the movie with the given condition for the certain kind within the movie. 
```
