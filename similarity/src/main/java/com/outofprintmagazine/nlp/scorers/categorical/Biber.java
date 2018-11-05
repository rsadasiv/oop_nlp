package com.outofprintmagazine.nlp.scorers.categorical;

import java.util.ArrayList;
import java.util.List;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.ScorerImpl;
import com.outofprintmagazine.nlp.scores.Score;

import edu.stanford.nlp.ling.CoreAnnotations.ParagraphIndexAnnotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;

public class Biber extends ScorerImpl implements DocumentCategoricalScorer {
		
//	public DocumentLength() {
//		super();
//	}
		
	public Biber(Ta ta) {
		super(ta);
	}

	@Override
	public List<Score> scoreDocument(CoreDocument document) {
		ArrayList<Score> retval = new ArrayList<Score>();
		double tokenCount = 0;
		double sentenceCount = 0;
		double paragraphCount = 0;
		
		for (CoreSentence sentence : document.sentences()) {
			sentenceCount++;
			paragraphCount = sentence.coreMap().get(ParagraphIndexAnnotation.class);
			tokenCount += sentence.tokens().size();
		}
        retval.add(new Score("tokenCount", tokenCount));
        retval.add(new Score("sentenceCount", sentenceCount));
        retval.add(new Score("paragraphCount", paragraphCount));
			
		return retval;
	}
	
	void PrivateVerbs()	{

	    Amplify::Module::List* privateVerbsList = List::Create("var/module/list/PrivateVerbsList.txt");
	    //privateVerbsList.Load("var/module/list/PrivateVerbsList.txt");

	    XalanNavigator pv(context->Manager,context->Document);

	    unsigned count=0;

	    // tag = “VB” && lemma = PrivateVerbsList  
	    pv.Select("//vb/@base");
	    count += static_cast<unsigned>(privateVerbsList->Contains(pv));

	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"PrivateVerbs",count);
	}
	
	void Contractions()
	{

	    XalanNavigator co(context->Manager,context->Document);

	    unsigned count=0;

	    // a)tok= “n’t”|“’ll”|“’d”|“’s” (for contractions predefined in “spellfixes”) 
	    //co.Select("//text()[.='n&apos;t' or .='&apos;ll' or .='&apos;d' or .='&apos;s'] ");
	    co.Select("//text()[.=\"n't\" or .=\"'ll\" or .=\"'d\" or .=\"'ve\" or .=\"'s\" or .=\"N't\" or .=\"'LL\" or .=\"'D\" or .=\"'VE\" or .=\"'S\" or .=\"N'T\"] ");
	    count += co.getLength();

	    // b) Other contractions (e.g. “Maria‘s laughing”) will not be separated from their word by the 
	    // tokenizer and need to be matched in the token (for instance using regular expressions). 
	    // “‘s” suffixed on nouns should be analyzed separately to exclude possessive forms. The 
	    // contractions should thus be followed by a verb or an adverb, not a noun phrase. 
	    // tag=”NN” && (tok match ”’s”) + (next token has) tag=”VB”|”RB” 
	    co.Select("//node()[name() = 'nn' or name() = 'pm']/text()[substring(.,string-length(.)-1) = \"'s\"]/following::node()[1][name() = 'vb' or name() = 'rb']");

	    count += co.getLength();

	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),
	                  "Contractions",
	                  count);
	}
	
	void PresentTenseVerbs()
	{

	    XalanNavigator pr(context->Manager,context->Document);

	    unsigned count=0;

	    // tag = “VB PRS SN3” | "VB PRS MOD" | "VB PRS SY1" | "VB PRS SY3"
	    pr.Select("//vb[(@msd = 'prs sn3' or @msd = 'prs sy3' or @msd = 'prs mod' or @msd = 'prs sy1')]");

	    count += pr.getLength();
		
	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"PresentTenseVerbs",count);
	}
	
	void SecondPersonPronouns() {

	    XalanNavigator sp(context->Manager,context->Document);

	    unsigned count=0;

	    // tag = “PP” && lemma = “you”|“your”|“yourself”| “yourselves” 
	    sp.Select("//pp[@base='you' or @base='your' or @base='yourself' or @base='yourselves']");
	    count += sp.getLength();

	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"SecondPersonPronouns",count);
	}
	
	void DoAsProverb() {

	    XalanNavigator da(context->Manager,context->Document);
	    XalanNavigator inner(context->Manager,context->Document);
	    XalanNavigator parent(context->Manager,context->Document);

	    unsigned count=0;
	    XalanNode *parentNode,*parentNode1;

	    // tag=”<<vs>>” / lemma=”do” 
	    // tag=”<<vmi>>” / lemma[last()] = ”do” 
	    // tag=”<<vhn>>” / lemma[last()] = ”do” 
	    // tag=”<<vdi>>” / lemma[last()] = ”do” 
	    // tag=”<<vbg>>” / lemma[last()] = ”do” 
	    // (These rules mean that the last lemma in a verb group should be "do". The <<vs>>-tag denotes a "simple" verb group (i.e. consisting of 1 word only), and it should contain "do".) 

	    da.Select("//node()[name() = 'vmi' or name() = 'vhn' or name() = 'vdi' or name() = 'vbg' or name() = 'vli']//vb[last()]/@base[.='do']");
	    count += da.getLength();

	    da.Select("//vs//vb[last()][@base='do']");
	    for (unsigned i=0;i<da.getLength();i++)
		{
	            parent.Select(da.item(i)->getParentNode(),"./ancestor::cl");
	            parentNode = parent.item(0);
	            //XalanLibrary::Print(parentNode, dbgStr);
	            //dbgStr.clear();
	            inner.Select(da.item(i)->getParentNode(),"./following::node()[name()='vb']");
	            if(!inner.getLength())
			{
	                    count++; //no other vb node .hence increment and mv to next
	                    continue;
			}
	            for(unsigned j=0;j<inner.getLength();j++)
			{
	                    XalanNode *node = inner.item(j);
	                    //XalanLibrary::Print(node, dbgStr);
	                    parent.Select(node,"./ancestor::cl");
	                    parentNode1 = parent.item(0);
	                    std::string attr = XalanLibrary::getAttrValue(node, "base");
	                    if( parentNode == parentNode1 )
				{
	                            if(attr == "do")
					{
	                                    count++; // this will be hit when cl has 1 "do" lemma node
	                                    break;
					}
				}
	                    else if( j==0)
				{
	                            count++; // this will be hit when only one VB node is there in cl and there are other cl with vb node
	                            break;
				}
			}
			
		}
	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"DoAsProVerb",count);
	}
	
	void AnalyticNegation()	{


	    XalanNavigator an(context->Manager,context->Document);

	    unsigned count=0;	
	    an.Select("//rb[@msd='neg'][@base='not']");
	    count += an	.getLength();

	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),
	                  "AnalyticNegation",
	                  count);
	}
	
	void Library::DemonstrativePronouns() {


	    XalanNavigator de(context->Manager,context->Document);

	    unsigned count=0;

	    // a) tok=”that”|“this”|“these”|“those” + (next token has) tag= “VB.*” 
	    de.Select("//text()[.='that' or .='this' or .='these' or .='those' or .='That' or .='This' or .='These' or .='Those' or .='THAT' or .='THIS' or .='THESE' or .='THOSE']/following::text()[1]/parent::vb");
	    count += de.getLength(); 

	    // b) tok=”that”|“this”|“these”|“those”” + tok= "who” | “whom” | “whose” | “which” | “and” 
	    de.Select("//text()[.='that' or .='this' or .='these' or .='those' or .='That' or .='This' or .='These' or .='Those' or .='THAT' or .='THIS' or .='THESE' or .='THOSE']/following::text()[1][.='who' or .='whom' or .='whose' or .='which' or .='and' or .='Who' or .='Whom' or .='Whose' or .='Which' or .='And' or .='WHO' or .='WHOM' or .='WHOSE' or .='WHICH' or .='AND']");
	    count += de.getLength();

	    // c) tok = “that” + tok = “’s” 
	    de.Select("//text()[.='that' or .='That' or .='THAT']/following::text()[1][.='&apos;s']");
	    count += de.getLength();

	    // d)tag=”<<cl>>” // tok=”that” (first token in the clause is "that") 
	    de.Select("//cl//text()[1][.='that' or .='That' or .='THAT']");
	    count += de.getLength();

	    // e) tok=”that”|“this”|“these”|“those” + tag= “<<cl>>” 
	    de.Select("//text()[.='that' or .='this' or .='these' or .='those' or .='That' or .='This' or .='These' or .='Those' or .='THAT' or .='THIS' or .='THESE' or .='THOSE']//following::cl[1]");
	    count += de.getLength();


	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"DemonstrativePronouns",count);
	}
	
	void FirstPersonPronouns() {

	    unsigned count=0;

	    // tag = “PP” && lemma =”I”|“we”|“my”|“our”|“myself”| “ourselves” 
	    fp.Select("//pp/@base[.='I' or .='we' or .='my' or .='our' or .='myself' or .='ourselves']");
	    count += fp.getLength();

	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"FirstPersonPronouns",count);
	}
	
	void PronounIt() {
	    unsigned count=0;

	    // tag = “PP” && lemma = “it” 
	    pr.Select("//pp[@base='it']");
	    count += pr.getLength();

	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"PronounIt",count);
	}
	
	void BeAsMainVerb() {

	    Amplify::Module::List* possPro = List::Create("var/module/list/PossProList.txt");
	    //possPro.Load("var/module/list/PossPro.txt");

	    XalanNavigator be(context->Manager,context->Document);

	    unsigned count=0;	

	    // a) lemma=“be” + (next token has) tag = ”DT”|”IN”|”JJ” 
	    be.Select("//node()[@base='be']/following::text()[1]/parent::node()[name() ='at' or name() = 'dt' or name() = 'in' or name() = 'jj']" );
	    count += be.getLength();

	    // b) lemma=“be” + lemma = PossPro 
	    be.Select("//node()[@base='be']/following::text()[1]/parent::node()/@base");
	    for (unsigned bei=0;bei<be.getLength();++bei)
		{
	            std::string lemma;
	            XalanNode * lemmaNode = be.item(bei);
	            if (lemmaNode && lemmaNode->getNodeType() == XalanNode::ATTRIBUTE_NODE)
			{
	                    XalanLibrary::ToString(lemmaNode->getNodeValue(),lemma);
	                    if (possPro->Contains(lemma))
	                        ++count;
			}
		}

	    // c) lemma=“be” + tok = ”Mr.”|“Mrs.”|“Ms.”|“Miss” 
	    be.Select("//node()[@base='be']/following::text()[1][.='Mr.' or .='Mrs.' or .='Ms.' or .='Miss' or .='mr.' or .='mrs.' or .='ms.' or .='miss' or .='MR.' or .='MRS.' or .='MS.' or .='MISS.']");
	    count += be.getLength();

	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"BeAsMainVerb",count);
	}
	
	void CausativeSubordination() {
	    unsigned count=0;

	    // tag = “CS” && tok = “because”|“since”|“for”|“as” 
	    an.Select("//cs/text()[1][.='because' or .='since' or .='for' or .='as' or .='BECAUSE' or .='SINCE' or .='FOR' or .='AS' or .='Because' or .='Since' or .='for' or .='As']");
	    count += an.getLength();

	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),
	                  "CausativeSubordination",
	                  count);
	}
	
	void DiscourseParticles() {

	    unsigned count=0;
		
	    // tag=”<cl>” // tok = “well”|“now”|“anyway”|“anyhow”| “anyways” 
	    // (first token in the clause is one of these) 
	    dp.Select("//cl//text()[1][.='well' or .='now' or .='anyway' or .='anyhow' or .='anyways' or .='WELL' or .='NOW' or .='ANYWAY' or .='ANYHOW' or .='ANYWAYS' or .='Well' or .='Now' or .='Anyway' or .='Anyhow' or .='Anyways']");
	    count += dp.getLength();

	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"DiscourseParticles",count);
	}

	void Library::IndefinitePronouns() {

	    unsigned count=0;

	    // tag = “PN NOM”|“PN GEN” 
	    ip.Select("//pn[@msd = 'nom' or @msd ='gen']");
	    count += ip.getLength();

	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"IndefinitePronouns",count);;
	}
	
	void GeneralHedges() {

	    unsigned count=0;

	    // a) tok=”at” + tok=”about” 
	    gh.Select("//text()[.='at' or .='AT' or .='At']/following::text()[1][.='about' or .='About' or .='ABOUT']");
	    count += gh.getLength();

	    // b) tok=”something” + tok=”like” 
	    gh.Select("//text()[.='something' or .='SOMETHING'  or .='Something' ]/following::text()[1][.='like' or .='Like' or .='LIKE']");
	    count += gh.getLength();

	    // c) tok=”more” + tok=”or” + tok=”less” 
	    gh.Select("//text()[.='more' or .='More' or .='MORE' ]/following::text()[1][.='or' or .='Or' or .='OR' ]/following::text()[1][.='less' or .='Less' or .='LESS']");
	    count += gh.getLength();

	    // d) tok=”almost” | ”maybe” 
	    gh.Select("//text()[.='almost' or .='maybe' or .='Almost' or .='Maybe'or .='ALMOST' or .='MAYBE'  ]");
	    count += gh.getLength();

	    // e) tok=”sort” | ”kind” + tok=”of” 
	    gh.Select("//text()[.='sort' or .='kind' or .='Sort' or .='Kind' or .='SORT' or .='KIND']/following::text()[1][.='of' or .='OF' or .='Of']");
	    count += gh.getLength();

	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"GeneralHedges",count);
	}
	
	//MAT - AMP
	void Library::Amplifiers() {

	    Amplify::Module::List* amplifiersList = List::Create("var/module/list/AmplifiersList.txt");
	    //amplifiersList.Load("var/module/list/AmplifiersList.txt");

	    XalanNavigator am(context->Manager,context->Document);

	    int count=0;
		
	    am.Select("descendant-or-self::node()[self::rb[@msd='pos'] or self::ql]/@base");
	    //count += am	.getLength();

	    // Count the number of words in the amplifers list

	    for (unsigned ami=0;ami<am.getLength();++ami)
		{
	            std::string lemma;
	            XalanNode * lemmaNode = am.item(ami);
	            if (lemmaNode && lemmaNode->getNodeType() == XalanNode::ATTRIBUTE_NODE)
			{	
	                    XalanLibrary::ToString(lemmaNode->getNodeValue(),lemma);
	                    if (amplifiersList->Contains(lemma))
	                        ++count;
			}
		}


	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),
	                  "Amplifiers",
	                  count);
	}
	
	void Library::WhQuestions(Context * context, LibraryOutput & output)
	{
	    if (output.Result("WhQuestions")) return;

	    Amplify::Module::List* wHList = List::Create("var/module/list/WHList.txt");
	    //wHList.Load("var/module/list/WHList.txt");

	    Amplify::Module::List* auxList = List::Create("var/module/list/AuxList.txt");
	    //auxList.Load("var/module/list/AuxList.txt");

	    XalanNavigator wh(context->Manager,context->Document);
	    XalanNavigator whnext(context->Manager,context->Document);

	    unsigned count=0;
	    // tag = ”<cl>” //([1]lemma = WHList + lemma = AuxList) 
	    // (first lemma in the clause is in WHList, second lemma in AuxList) 
	  
	    wh.Select("//cl//text()[1]/parent::node()/@base");
	    for (unsigned whi=0;whi<wh.getLength();++whi)
		{
	            // Check the first nodes lemma
	            XalanNode * attribute = wh.item(whi);
	            if (attribute && wHList->Contains(attribute))
			{
	                    // Select the following nodes lemma
	                    attribute = whnext.Evaluate(attribute,"parent::node()/following::text()[1]/parent::node()/@base");
	                    if (attribute && auxList->Contains(attribute))
	                        ++count;
			}
		}

	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"WhQuestions",count);
	}
	
	void PossibilityModals() {

	    unsigned count=0;

	    // tag = “VB.*MOD” && lemma = “can”|“may” 

	    pm.Select("//vb[@base='can' or  @base = 'may']/@msd");

	    for (unsigned pmi=0;pmi<pm.getLength();++pmi)
		{
	            if (XalanLibrary::Matches(pm.item(pmi),"* mod"))
			{
	                    ++count;
			}
		}

	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"PossibilityModals",count);	
	}
	
	void Nouns() {
	    unsigned count=0;

	    // tag = “NN” 
	    nn.Select("//nn");
	    count += nn.getLength();

	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"Nouns",count);
	}
	
	//MAT AWL
	void MeanWordLength() {

	    // Word length is defined as the number of characters of a word. 
	    // The mean word length is calculated as the sum of all word lengths
	    // divided by the total number of words in the text. 

	    XalanNavigator nwc(context->Manager,context->Document);
	    Amplify::Module::List* punctuationList = List::Create("var/module/list/PunctuationList.txt");

	    int words = 0;
	    unsigned lengths = 0;
	    std::string word;

	    nwc.Select("//text()");
	    for (unsigned nwci=0;nwci<nwc.getLength();++nwci)
		{
	            if (punctuationList->Contains(nwc.item(nwci)))
	                continue;
	            XalanNode *node = nwc.item(nwci);
	            word.clear();
	            XalanLibrary::Print(node, word);
	            lengths += word.length();
	            words++;
		}
	    double mean;
	    if(words == 0)
	        mean = 0;
	    else
	        mean =  (double)lengths / (double)words;

	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"MeanWordLength",mean);	
	}
	
	void Prepositions() {

	    unsigned count=0;

	    // tag = “IN”
	    pr.Select("//in");
	    count += pr.getLength();

	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"Prepositions",count);
	}
	
	void NumberOfTypes() {
	    Amplify::Module::List* punctuationList = List::Create("var/module/list/PunctuationList.txt");

	    unsigned count=0;

	    // Count total number of unique words (tokens) in the text. 
	    // E.g. “The tall man saw the short man” has 5 types: the, tall, man, saw, short 

	    std::set<std::string> words;

	    nt.Select("//text()");
	    for (unsigned nti=0;nti<nt.getLength();++nti)
		{
	            if (punctuationList->Contains(nt.item(nti)))
	                continue;
	            std::string word;
	            XalanNode * txt = nt.item(nti);
	            XalanLibrary::ToString(txt->getNodeValue(),word);
	            Lowercase(word);
	            words.insert(word);
		}

	    count += words.size();

	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"NumberOfTypes",count);	
	}
	
	void NumberOfWords() {

	    unsigned count=0;
	    int countPunctuation = 0;

	    // Count total number of words (tokens) in the text. 
		
	    nt.Select("//text()");
		
	    count += nt.getLength();
		
	    Execute(context,
	            output,
	            "PunctuationMarks",
	            countPunctuation);
		
	    count = count - static_cast<unsigned>(countPunctuation);
		
	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"NumberOfWords",count);	
	}
	
	//MAT - TTR
	void TypeTokenRatio() {

	    // Return the ratio: NumberOfTypes / NumberOfWords 

	    int countNumberOfTypes = 0;
	    Execute(context,output,"NumberOfTypes",countNumberOfTypes);

	    int countNumberOfWords = 0;
	    Execute(context,output,"NumberOfWords",countNumberOfWords);
	    double ratio;
	    if(countNumberOfWords == 0)
	        ratio = 0;
	    else
	        ratio = (double)countNumberOfTypes / (double)countNumberOfWords;

	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"TypeTokenRatio",ratio);	
	}
	
	void AttributiveAdjectives() {

	    unsigned count=0;	

	    // tag = “JJ” + tag = “JJ”|“NN”|”FW” 
	    aa.Select("//jj/following::text()[1]/parent::node()[self::jj or self::nn or self::fw]");
	    count += aa	.getLength();

	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),
	                  "AttributiveAdjectives",
	                  count);
	}
	
	private void BiberFactor1()	{

	    // Factor 1 Positive features 
	    // private verbs, contractions, present tense verbs, 2nd person pronouns, DO as pro-verb, 
	    // analytic negation, demonstrative pronouns, general emphatics, 1st person pronouns,
	    // pronoun IT, BE as main verb, causative subordination, discourse particles, 
	    // indefinite pronouns, general hedges, amplifiers, WH questions, possibility modals 
		
	    int positive = 0;
	    // Execute sums the output value, so theres no need to use a temporary
	    Execute(context,output,"PrivateVerbs",positive);
	    Execute(context,output,"Contractions",positive);
	    Execute(context,output,"PresentTenseVerbs",positive);
	    Execute(context,output,"SecondPersonPronouns",positive);
	    Execute(context,output,"DoAsProverb",positive);
	    Execute(context,output,"AnalyticNegation",positive);
	    Execute(context,output,"DemonstrativePronouns",positive);
	    Execute(context,output,"FirstPersonPronouns",positive);
	    Execute(context,output,"PronounIt",positive);
	    Execute(context,output,"BeAsMainVerb",positive);
	    Execute(context,output,"CausativeSubordination",positive);
	    Execute(context,output,"DiscourseParticles",positive);
	    Execute(context,output,"IndefinitePronouns",positive);
	    Execute(context,output,"GeneralHedges",positive);
	    Execute(context,output,"Amplifiers",positive);
	    Execute(context,output,"WhQuestions",positive);
	    Execute(context,output,"PossibilityModals",positive);
		
	    // Factor 1 Negative features 
	    // nouns, word length, prepositions, type/token ratio, attributive adjectives
		
	    int negative = 0;
	    Execute(context,output,"Nouns",negative);
	    Execute(context,output,"MeanWordLength",negative);
	    Execute(context,output,"Prepositions",negative);
	    Execute(context,output,"TypeTokenRatio",negative);
	    Execute(context,output,"AttributiveAdjectives",negative);
				
		
	    int factor = positive - negative;

	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"BiberFactor1",factor);
	}

	void BiberFactor2() {

	
	    // Factor 2 Positive features  
	    // past tense verbs, third person pronouns, perfect aspect verbs, public verbs
	    int positive = 0;
	    Execute(context,output,"PastTenseVerbs",positive);
	    Execute(context,output,"ThirdPersonPronouns",positive);
	    Execute(context,output,"PerfectAspect",positive);
	    Execute(context,output,"PublicVerbs",positive);

	    // Factor 2 Negative features
	    // none to be included  
	    int negative = 0;

	    int factor = positive - negative;

	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"BiberFactor2",factor);
	}

	void BiberFactor3()	{


	    // Factor 3 Positive features    
	    // none to be included  
	    int positive = 0;
		
	    // Factor 3 Negative features
	    // time adverbials, place adverbials, adverbs  
	    int negative = 0;
	    Execute(context,output,"TimeAdverbials",negative);
	    Execute(context,output,"PlaceAdverbials",negative);
	    Execute(context,output,"Adverbs",negative);
		
	    int factor = positive - negative;
	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"BiberFactor3",factor);
	}

	void BiberFactor4()	{


	    // Factor 4 Positive features 
	    // infinitives, prediction modals, suasive verbs, conditional subordination, necessity modals, split auxiliaries
	    int positive = 0;
	    Execute(context,output,"Infinitives",positive);
	    Execute(context,output,"PredictionModals",positive);
	    Execute(context,output,"SuasiveVerbs",positive);
	    Execute(context,output,"ConditionalSubordination",positive);
	    Execute(context,output,"NecessityModals",positive);
	    Execute(context,output,"SplitAuxiliaries",positive);

	    // Factor 4 Negative features 
	    // No negative features 
	    int negative = 0;

	    int factor = positive - negative;
	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"BiberFactor4",factor);
	}

	void BiberFactor5() {

	    // Factor 5 Positive features 
	    // conjuncts, agent less passives, past participial clauses, BY-passives
	    int positive = 0;
	    Execute(context,output,"Conjuncts",positive);
	    Execute(context,output,"AgentlessPassives",positive);
	    Execute(context,output,"ByPassives",positive);

	    // FIXME
	    // There was no definition for this function available
	    // PastParticiple 

	    // Factor 5 Negative features
	    // type/token ratio 
	    int negative = 0;
	    Execute(context,output,"TypeTokenRatio",negative);

	    int factor = positive - negative;
	    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"BiberFactor5",factor);
	}


}
