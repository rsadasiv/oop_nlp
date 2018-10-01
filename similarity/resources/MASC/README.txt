MULTI-ANNOTATED WORD SENSE DATA
======================================================================

Annotation Task
----------------------------------------------------------------------
Given an instance of a word in a sentence, provide the intended
sense from a list of dictionary-style senses (from WordNet 3.0).

The instructions to annotators were:

    For the 10 sentences in this HIT, select the best meaning of the word
    in boldface. Each sentence is followed by the same list of meanings to
    choose from.

An example sentence plus list of meanings for the noun form of "date" is:

    Sentence 6:   
    One of my first dates with the young woman who was to
    be my wife took us to Les Sylphides downtown in the Loop.

   1.  the specified day of the month    
       Example: what is the date today?

   2.  a participant in a date  
       Example: his date never stopped talking

   3.  a meeting arranged in advance    
       Example: she asked how to avoid  kissing at the end of a date

   4.  a particular but unspecified point in time    
       Example: they hoped to get together at an early date

   5.  the present    
       Example: they are up to date

   6.  the particular day, month, or year (usually according to the
           Gregorian calendar) that an event occurred    
       Example: he tried to memorizes all the dates for his history class

   7.  a particular day specified as the time something happens    
       Example: the date of the election is set by law

   8.  sweet edible fruit of the date palm with a single long woody seed

   9.  Not clear, or none of the above

A sample HIT, Work10.html, is included with the release (see below under
"Annotations and Annotators").

Words
----------------------------------------------------------------------
The 45 words annotated include a mix of nouns (n), verbs (v), 
and adjectives (j):

  add-v, appear-v, ask-v, board-n, book-n, color-n, common-j, control-n,
  date-n, fair-j, family-n, find-v, fold-v, full-j, help-v, high-j,
  image-n, kill-v, know-v, land-n, late-j, level-n, life-n, live-v, 
  long-j, lose-v, meet-v, normal-j, number-n, paper-n, particular-j,
  poor-j, read-v, say-v, sense-n, serve-v, show-v, suggest-v,
  tell-v, time-n, wait-v, way-n, win-v, window-n, work-n

Dictionary of Senses
----------------------------------------------------------------------
The dictionary used to define the senses that were annotated is:

  - WordNet: http://wordnet.princeton.edu
    Pre-release version between 3.0 and 3.1,
    often the same as 3.0.

    The senses seen by the turkers for this annotation task are
    identical to the senses seen by trained MASC annotators
    for the MASC word sense sentence corpus (described in
    Passonneau, Rebecca J.; Baker, Collin; Fellbaum, Christiane;
        Ide, Nancy. 2012. The MASC word sense sentence corpus.
        Proceedings of the 8th International Conference on Language
        Resources and Evaluation (LREC), pp. 3025-030. Istanbul, Turkey. 
        May 23-25. ELRA.


Sentences
----------------------------------------------------------------------
Approximately 1000 instances per word are drawn from texts in a 
range of genres from:

  -- Manually Annotated Sub-Corpus (MASC)
     http://www.anc.org/MASC/Home.html

and where MASC had insufficient instances, the remainder are drawn from:

  -- American National Corpus (ANC)
     http://www.anc.org     


Annotations and Annotators
----------------------------------------------------------------------
Each word was labeled by approximately 25 different annotators,
for a total of roughly 1M total annotations. 

The annotators submitted work in batches of 10 instances of a single
word on a web form (Amazon Mechanical Turk Human Intelligence Task).
Up to 100 forms were available for each word, for a total of up to
1000 instances per word. The release includes a sample html page for
one of the HITs: Work10.html.

The annotators were 229 workers drawn from Amazon Mechanical Turk.  No
demographic information on the annotators is available, but the HIT
properties required workers to be from the US, and to have 99%
acceptance of previous HITs with at least 20K total HITs accepted
(this was instituted after the 7th word; see below). Also, the HIT
title and description specified that HITs should be done only by
native speakers of English.

Several workers were prevented from continuing due to poor initial
work (as measured by agreement with the majority label), but their
noisy annotations remain in the data.

The data was annotated in three phases, 2 words in phase 1, 5 words in
phase 2, and the remainder in phase 3.  The purpose of the first two
phases was to determine the best task design, constraints on turker
qualifications, and impact of bonus payments.  The words add-v and
book-n were annotated during phase 1; appear-v, ask-v, board-n,
common-j, high-j were annotated during phase 2. In phases 1-2, bonuses
were advertised and paid to workers for every instance annotated
beyond the first 100 for a word, with an additional bonus for each
instance annotated beyond the first 400.  For phase 3, we moved to
high qualifications on turkers with no bonus, and raised the pay per
HIT.


Data Files and Format
----------------------------------------------------------------------
The data is distributed in tab-separated value format with the
following fields.  Headers are included as the first line of each
file.

    masc/AMT/RELEASE/annotations.tsv
    --------------------------------
    WordPos
    FormId
    SentenceId
    AnnotatorId
    SenseId

    masc/AMT/RELEASE/sentences.tsv
    --------------------------------
    WordPos
    FormId
    SentenceId
    Text
    AncDocPath
    SentenceStart
    WordStart
    WordEnd
    WordForm

    masc/AMT/RELEASE/senses.tsv
    --------------------------------
    WordPos 
    SenseId 
    SenseDef 
    SenseExamples
    WordNetId
    WordNetSynonyms

The contents of the fields are:

   WordPos          string   word and part-of-speech pair (separated
                             by hyphen) 
   SenseId          integer  WordNet sense identifier for word, or 0
                             if none apply 
   SenseDef         string   WordNet definition of sense
   SenseExamples    string   example sentences using word in sense
   WordNetId        string   identifier of word meaning in WordNet
   WordNetSynonyms  string   identifiers of synonymous word meanings,
                             and Wordnet sense key, e.g., "add#1 (add%2:30:00::)"
   SentenceId       integer  position of sentence in sequence of
                             sentences on a form 
   FormId           integer  position of web form in sequence of forms
   AnnotatorId      integer  identifier for annotator
   SenseId          integer  WordNet Sense ID
   Text             string   text of sentence
   AncDocPath       string   path to document for sentence in ANC
   SentenceStart    integer  index of first character of sentence in
                             doc 
   WordStart        integer  index of first character of word in sent 
   WordEnd          integer  index of last character of word in sent
   WordForm         string   instance of the word, including
                             original capitalization and inflection

A few SentenceStart, WordStart and WordEnd indexes are unknown and
listed as -1.
  

Authors
----------------------------------------------------------------------

Rebecca J. Passonneau
Columbia University, Center for Computational Learning Systems (CCLS)

Bob Carpenter
Columbia University, Deptartment of Statistics


Acknowledgments
----------------------------------------------------------------------
Thanks to Marilyn Walker for suggesting the bboard Turker Nation and
thanks to the participants in our discussion there for suggesting
improvements to our task instructions, compensation, and filtering.

Thanks to Shreya Prasad for Java coding for Mechanical Turk and
parsing outputs.  Thanks to Mitzi Morris for Java coding for
string-based linkage of sentences between Mechanical Turk forms and
the ANC database.  

Thanks to Nancy Ide and Tim Brown for spot-checking the consistency
with AMT and suggesting improvements to our README.

Thanks also to our funding agencies.  Passonneau was supported by NSF
CRI 0708952 and CRI 1059312.  Carpenter was supported by NSF
CNS-1205516 and DoE DE-SC0002099.
