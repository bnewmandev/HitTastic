#  Background:
#    Given the following songs are loaded
#      | "title"            | "artist"        |
#      | "Faded And Heaven" | "Yassin Bates"  |
#      | "Longing For Past" | "Christy Weber" |
#      | "Wild And Vibes"   | "Tamar Marks"   |
#      | "We Can Dance"     | "Todd French"   |
#      | "Picture Of Fame"  | "Julie Knapp"   |


Feature: Search

  Scenario Outline: Search songs by title
    Given No songs are selected
    When Title search is run with <phrase>
    Then results has a length of <length>
    Examples:
      | phrase       | length |
      | ""           | 5      |
      | "Faded"      | 1      |
      | "faded"      | 1      |
      | "and"        | 2      |
      | "angel"      | 0      |
      | "Picture of" | 1      |

  Scenario Outline: Search songs by artist
    Given No songs are selected
    When Artist search is run with <phrase>
    Then results has a length of <length>
    Examples:
      | phrase | length |
      | ""     | 5      |
      | "T"    | 4      |
      | " "    | 5      |
      | "todd" | 1      |
      | "dave" | 0      |

