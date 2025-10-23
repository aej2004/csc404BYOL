# BYOL
## About Language: 

<p><em>Language Name:</em> Mov</p>

<p><em>Filename Extension:</em> .mov</p>

<p><em>Description:</em> Mov is a specified-purpose programming language that has the purpose of access and interpreting information about movies and all that encompasses them. It's syntax is designed to be straightforward and easy to read for the user. Allowing the user the ability to access a movie data base of movie reviews and information to complete certain task. This language is ideal for movie reviewers and watchers that want a way to sort through movies and access the information that comes with them.</p>

<p>Link To Database: https://www.kaggle.com/datasets/rajugc/imdb-movies-dataset-based-on-genre?resource=download </p>

## Grammar: 

```
findStmt -> "find" kind query str (where | without. <condition>)

haveStmt -> "have" ... = <findStmt>

sayStmt -> "say" str (rating | summary) (num | str)

writeStmt -> "write" kind query str (where | without. <condition>)

kind -> "movies" | "ratings" | "genre" | "stars" | "year" | "summary" | "length" | "director" | age-appropriate

query -> "starring" | "directed by" | "for" | "in" | "of"

condition -> str | kind "is" str | <conditon> > <condition>
```

## Examples: 

### Example Program 1 Getter

```
find movies starring "Tom Hanks"
find movies directed by "Chris Columbus"
find genre for "Tom Hanks"
find stars in "The Lion King"
find genre for "The Lion King"
find summary of "The Lion King"
find length of "The Little Mermaid"
```

<p>Outputs:</p>

```
The string or the list of strings that go with the kind and string given. 
```

### Example Program 2 Declaration

```
have tomMovies = find movies with "tom hanks"
find stars in tomMovies without "tom hanks"
have lkActors = find stars in "The Lion King"
find movies in lkActors without "The Lion King"
```

<p>Outputs:</p>

```
Stores the string, list of strings, or double under the given variables name. 
```

### Example Program 3 Setter

```
say "Sing 2" rating 9.5
find rating for "Tori Kelly"
```

<p>Outputs:</p>

```
Returns the average rating for Tori Kelly changed to include the new rating the movie "Sing 2" that she's in.
```

```
say "The Little Mermaid" summary "A great child friendly movie about ..."
find summary for "The Little Mermaid"
```

<p>Outputs:</p>

```
Returns the new summary for the film that the person just wrote. 
```

### Example Program 4 Return/Print Output

```
write movies starring "Tom Hanks"
write stars in "The Lion King" where movie is "The Little Mermaid"
```

<p>Outputs:</p>

```
outputs all the movies that star Tom Hanks
outputs all the stars that are in both movies
```

### Example Program 5 Conditionals

```
find movies with "tom hanks" where genre is "action"
find stars in "The Lion King" where movies is "The Little Mermaid"
```

<p>Outputs:</p>

```
returns the movie with the given condition for the certain kind within the movie. 
```
