#include "amplify/feature/library.h"
#include "amplify/feature/education.h"
#include "amplify/module/list.h"
#include "amplify/module/scorelist.h"
#include "amplify/module/ontology.h"
#include "amplify/feature/nounphrase.h"
#include "amplify/xalan.h"
#include "amplify/string.h"
#include <algorithm>
#include <string.h>
#include <iostream>
#include <string.h>
#include <algorithm>
#include <iostream>
#include <stack>
#include "amplify/string.h"
#include "amplify/environment.h"
#include "amplify/thread_stream.h"



using namespace Amplify;
using namespace Amplify::Module;

namespace Amplify { namespace Feature {

void LibraryOutput::Result(int code, const char * feature, int value)
{
    std::stringstream result;
    result << value;
    Features[feature] = std::make_pair(code,result.str());	
}

void LibraryOutput::Result(int code, const char * feature, unsigned value)
{
    std::stringstream result;
    result << value;
    Features[feature] = std::make_pair(code,result.str());	
}

void LibraryOutput::Result(int code, const char * feature, double value)
{
    std::stringstream result;
    result << value;
    Features[feature] = std::make_pair(code,result.str());	
}

void LibraryOutput::Result(int code, const char * feature, std::string value)
{
    std::stringstream result;
    result << value;
    Features[feature] = std::make_pair(code,result.str());	

}

bool LibraryOutput::Result(const char * feature)
{
    std::map<std::string,std::pair<int,std::string> >::iterator result;
    result = Features.find(feature);
    return result != Features.end() &&
        (*result).second.first == static_cast<int>(LibraryOutput::SUCCESS);
}


const int Library::Id = 0;

Library::Library()
{
    // Map all the functions in the library to their string names

    Functions["ActiveVerbsWithAgentPresFuture"] = (Function)&Library::ActiveVerbsWithAgentPresFuture; 
    Functions["ActiveVerbsWithAgentPast"] = &Library::ActiveVerbsWithAgentPast; 
    Functions["ImperativeVerbs"] = &Library::ImperativeVerbs;
    Functions["Adverbs"] = &Library::Adverbs;
    Functions["AgentlessPassives"] = &Library::AgentlessPassives;
    Functions["Amplifiers"] = &Library::Amplifiers;
    Functions["AnalyticNegation"] = &Library::AnalyticNegation;
    Functions["AttributiveAdjectives"] = &Library::AttributiveAdjectives;
    Functions["BeAsMainVerb"] = &Library::BeAsMainVerb;
    Functions["ByPassives"] = &Library::ByPassives;
    Functions["CausativeSubordination"] = &Library::CausativeSubordination;
    Functions["Conjuncts"] = &Library::Conjuncts;
    Functions["Contractions"] = &Library::Contractions;
    Functions["DemonstrativePronouns"] = &Library::DemonstrativePronouns;
    Functions["DiscourseParticles"] = &Library::DiscourseParticles;
    Functions["DoAsProVerb"] = &Library::DoAsProVerb;
    Functions["FirstPersonPronouns"] = &Library::FirstPersonPronouns;
    Functions["GeneralEmphatics"] = &Library::GeneralEmphatics;
    Functions["GeneralHedges"] = &Library::GeneralHedges;
    Functions["IndefinitePronouns"] = &Library::IndefinitePronouns;
    Functions["Nouns"] = &Library::Nouns;
    Functions["PastTenseVerbs"] = &Library::PastTenseVerbs;
    Functions["PerfectAspect"] = &Library::PerfectAspect;
    Functions["PlaceAdverbials"] = &Library::PlaceAdverbials;
    Functions["Prepositions"] = &Library::Prepositions;
    Functions["PresentTenseVerbs"] = &Library::PresentTenseVerbs;
    Functions["PrivateVerbs"] = &Library::PrivateVerbs;
    Functions["PronounIt"] = &Library::PronounIt;
    Functions["PublicVerbs"] = &Library::PublicVerbs;
    Functions["SecondPersonPronouns"] = &Library::SecondPersonPronouns;
    Functions["ThirdPersonPronouns"] = &Library::ThirdPersonPronouns;
    Functions["TimeAdverbials"] = &Library::TimeAdverbials;
    Functions["WhQuestions"] = &Library::WhQuestions;
    Functions["SplitAuxiliaries"] = &Library::SplitAuxiliaries;
    Functions["ConditionalSubordination"] = &Library::ConditionalSubordination;
    Functions["Infinitives"] = &Library::Infinitives;
    Functions["NecessityModals"] = &Library::NecessityModals;
    Functions["PossibilityModals"] = &Library::PossibilityModals;
    Functions["PredictionModals"] = &Library::PredictionModals;
    Functions["SuasiveVerbs"] = &Library::SuasiveVerbs;
    Functions["ContrastiveDiscourseConnectives"] = &Library::ContrastiveDiscourseConnectives;
    Functions["SubjectiveVerbs"] = &Library::SubjectiveVerbs;
    Functions["CertaintyAdverbs"] = &Library::CertaintyAdverbs;
    Functions["DecisivenessMarkers"] = &Library::DecisivenessMarkers;
    Functions["DefiniteArticles"] = &Library::DefiniteArticles;
    Functions["IndefiniteArticles"] = &Library::IndefiniteArticles;
    Functions["NPs"] = &Library::NPs;
    Functions["NumberOfTypes"] = &Library::NumberOfTypes;
    Functions["NumberOfWords"] = &Library::NumberOfWords;
    Functions["Pronouns"] = &Library::Pronouns;
    Functions["MeanDefiniteArticles"] = &Library::MeanDefiniteArticles;
    Functions["DefiniteIndefiniteRatio"] = &Library::DefiniteIndefiniteRatio;
    Functions["PronounsNpRatio"] = &Library::PronounsNpRatio;
    Functions["NounsNpRatio"] = &Library::NounsNpRatio;
    Functions["ModifierWords"] = &Library::ModifierWords;
    Functions["ModifiersNPsRatio"] = &Library::ModifiersNPsRatio;
    Functions["MeanSyllables"] = &Library::MeanSyllables;
    Functions["MeanPrepositionalPhrases"] = &Library::MeanPrepositionalPhrases;
    Functions["MeanSyntacticDepth"] = &Library::MeanSyntacticDepth;
    Functions["TypeTokenRatio"] = &Library::TypeTokenRatio;
    Functions["MeanSentenceLength"] = &Library::MeanSentenceLength;
    Functions["EmotionWords"] = &Library::EmotionWords;
    Functions["FlamboyanceWords"] = &Library::FlamboyanceWords;
    Functions["LongAdjectivesOrAdverbs"] = &Library::LongAdjectivesOrAdverbs;
    Functions["FemaleWords"] = &Library::FemaleWords;
    Functions["MaleWords"] = &Library::MaleWords;
    Functions["CommonWords"] = &Library::CommonWords;
    Functions["PunctuationMarks"] = &Library::PunctuationMarks;
    Functions["SlangWords"] = &Library::SlangWords;
    Functions["YesNoQuestions"] = &Library::YesNoQuestions;
    Functions["OfferingGuidancePhrases"] = &Library::OfferingGuidancePhrases;
    Functions["RequestingGuidancePhrases"] = &Library::RequestingGuidancePhrases;
    Functions["ExplicitTemporalReferences"] = &Library::ExplicitTemporalReferences;
    Functions["IntentionVerbs"] = &Library::IntentionVerbs;
    Functions["BiberFactor1"] = &Library::BiberFactor1;
    Functions["BiberFactor2"] = &Library::BiberFactor2;
    Functions["BiberFactor3"] = &Library::BiberFactor3;
    Functions["BiberFactor4"] = &Library::BiberFactor4;
    Functions["BiberFactor5"] = &Library::BiberFactor5;

    //new..ones
    Functions["Adjectives"] = &Library::Adjectives;
    Functions["Events"] = &Library::Events;
    Functions["TemporalPhrases"] = &Library::TemporalPhrases;
    Functions["FirstOrSecondPersonPronounAsSubject"] = &Library::FirstOrSecondPersonPronounAsSubject;
    Functions["FutureReference"] = &Library::FutureReference;


    Functions["YoungCount"] = &Library::YoungCount;
    Functions["YoungBigramCount"] = &Library::YoungBigramCount;
    Functions["YoungRatio"] = &Library::YoungRatio;
    Functions["SlangRatio"] = &Library::SlangRatio;
    Functions["SeniorCount"] = &Library::SeniorCount;
    Functions["SeniorBigramCount"] = &Library::SeniorBigramCount;
    Functions["SeniorRatio"] = &Library::SeniorRatio;
    Functions["FemaleRatio"] = &Library::FemaleRatio;
    Functions["MaleRatio"] = &Library::MaleRatio; 
    Functions["RatioNpmodToNp"] = &Library::RatioNpmodToNp; 
    Functions["CommonWordsRatioNew"] = &Library::CommonWordsRatioNew; 
    Functions["FleschKincaidGradeLevel"] = &Library::FleschKincaidGradeLevel;  
    Functions["MeanWordLength"] = &Library::MeanWordLength;
    Functions["PercentageFlamboyantModifiers"] = &Library::PercentageFlamboyantModifiers;
    Functions["PercentageLongAdjectivesAndAdverbs"] = &Library::PercentageLongAdjectivesAndAdverbs;
    Functions["NumberOfSyllables"] = &Library::NumberOfSyllables;

    Functions["HumanOrCollectiveAgents"] = &Library::HumanOrCollectiveAgents;
    Functions["IWeNeedTo"] = &Library::IWeNeedTo; 
    Functions["LongAdjectives"] = &Library::LongAdjectives; 
    Functions["LongAdverbs"] = &Library::LongAdverbs; 
    Functions["ModifiersWithinNPs"] = &Library::ModifiersWithinNPs; 
    Functions["Negation"] = &Library::Negation; 
    Functions["NumberOfClauses"] = &Library::NumberOfClauses; 
    Functions["NumberOfNPmod"] = &Library::NumberOfNPmod; 
    Functions["NumberOfSentences"] = &Library::NumberOfSentences; 
    Functions["ObjectNPs"] = &Library::ObjectNPs; 
    Functions["OfferingGuidanceNecessityModals"] = &Library::OfferingGuidanceNecessityModals; 
    Functions["OfferingGuidancePredictionAndPossibilityModals"] = &Library::OfferingGuidancePredictionAndPossibilityModals; 
    Functions["Passives"] = &Library::Passives; 
    Functions["StativeVerbs"] = &Library::StativeVerbs; 
    Functions["SubjectNPs"] = &Library::SubjectNPs; 
    Functions["NegativeWords"] = &Library::NegativeWords; 
    Functions["PositiveWords"] = &Library::PositiveWords; 
    Functions["OntologyCategories"] = &Library::OntologyCategories; 
    clauseboundary = "./ancestor::cl";
}

Library::~Library()
{
    clauseboundary= 0;
}

bool Library::Execute(Context * context)
{
    // Call all library routines in succession and place library output class in the context

    LibraryOutput * output = new LibraryOutput(context);
    output->start_tv = context->start_tv;

    std::map<std::string,Function>::iterator func = Functions.begin();
    while (func != Functions.end())
	{
            if (Execute(context,*output,(*func).first.c_str()))
		{
                    std::pair<int,std::string> result = output->Features[(*func).first];
		}
            ++func;
	}
	
    context->Output(FEATURE_LIBRARY) = output;

    return true;
}	

bool Library::Execute(Context * context, LibraryOutput & output, const char * feature)
{
    // Execute with no return value

    if (Functions.find(feature) != Functions.end())
	{
            std::pair<int,std::string> result = output.Features[feature];
            if (result.first != static_cast<int>(LibraryOutput::SUCCESS))
		{
                    Function func = Functions[feature];
                    (this->*(func))(context,output);
			
                    result = output.Features[feature];
                    if (result.first != static_cast<int>(LibraryOutput::SUCCESS))
                        return false;

                    return true;
		}

            return true;	
	}

    return false;
}

bool Library::Execute(Context * context, LibraryOutput & output, const char * feature, int & value)
{
    // Execute a feature and return the result value from the output
    //std::map<std::string,std::pair<int,std::string> >::iterator iter;

    if (Functions.find(feature) != Functions.end())
	{
            std::pair<int,std::string> result = output.Features[feature];
            if (result.first != static_cast<int>(LibraryOutput::SUCCESS))
		{
                    Function func = Functions[feature];
                    (this->*(func))(context,output);
		
                    result = output.Features[feature];
                    if (result.first != static_cast<int>(LibraryOutput::SUCCESS))
                        return false;
		}
            int val = 0;			
            std::stringstream stream(result.second);
            stream >> val;
            value += val;

            return true;
		
	}

    return false;
}

bool Library::Execute(Context * context, LibraryOutput & output, const char * feature, double & value)
{
    if (Functions.find(feature) != Functions.end())
	{
            std::pair<int,std::string> result = output.Features[feature];
            if (result.first != static_cast<int>(LibraryOutput::SUCCESS))
		{
                    Function func = Functions[feature];
                    (this->*(func))(context,output);
			
                    result = output.Features[feature];
                    if (result.first != static_cast<int>(LibraryOutput::SUCCESS))
                        return false;

                    double val = 0;
                    std::stringstream stream(result.second);
                    stream >> val;
                    value += val;

                    return true;
		}
	}

    return false;
}

bool Library::Execute(Context * context, LibraryOutput & output, const char * feature, std::string & value)
{
    if (Functions.find(feature) != Functions.end())
	{
            std::pair<int,std::string> result = output.Features[feature];
            if (result.first != static_cast<int>(LibraryOutput::SUCCESS))
		{
                    Function func = Functions[feature];
                    (this->*(func))(context,output);
			
                    result = output.Features[feature];
                    if (result.first != static_cast<int>(LibraryOutput::SUCCESS))
                        return false;

                    std::string val;
                    std::stringstream stream(result.second);
                    stream >> val;
                    value.append(val);

                    return true;
		}
	}

    return false;
}


void Library::ActiveVerbsWithAgentPresFuture(Context * context, LibraryOutput & output)
{
    if (output.Result("ActiveVerbsWithAgentPresFuture")) return;

    // “verb phrases” in this output meaning TA outputted verb groups <vs>, <vbg>, <vhn>, <vmi> and <vdi>.

    Amplify::Module::List* stativeVerbsList = List::Create("var/module/list/StativeVerbs.txt");
    //stativeVerbsList.Load("var/module/list/StativeVerbs.txt");

    XalanNavigator vb(context->Manager,context->Document);
    //vb.Select("//vb");
    vb.Select("//vb/@base");

    int count=0;

    for (unsigned vbi=0;vbi<vb.getLength();++vbi)
	{	
            if (!stativeVerbsList->Contains(vb.item(vbi)))
                ++count;
	}

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),
                  "ActiveVerbsWithAgentPresFuture",
                  count);
}


void Library::ActiveVerbsWithAgentPast(Context * context, LibraryOutput & output)
{
    if (output.Result("ActiveVerbsWithAgentPast")) return;

    Amplify::Module::List* stativeVerbsList = List::Create("var/module/list/StativeVerbs.txt");
    //stativeVerbsList.Load("var/module/list/StativeVerbs.txt");

    XalanNavigator vb(context->Manager,context->Document);
    vb.Select("//vb");

    int count=0;
    for (unsigned vbi=0;vbi<vb.getLength();++vbi)
	{
            XalanNode * verbNode = vb.item(vbi);

            std::string base;
            XalanNode * baseNode = vb.Evaluate(verbNode,"@base");
            if (baseNode && baseNode->getNodeType() == XalanNode::ATTRIBUTE_NODE)
		{
                    XalanLibrary::ToString(baseNode->getNodeValue(),base);
                    if (!stativeVerbsList->Contains(base))
			{
                            ++count;
			}
		}
	}


    output.Result(static_cast<int>(LibraryOutput::SUCCESS),
                  "ActiveVerbsWithAgentPast",
                  count);
}


void Library::ImperativeVerbs(Context * context, LibraryOutput & output)
{
    if (output.Result("ImperativeVerbs")) return;

    XalanNavigator imp(context->Manager,context->Document);

    unsigned count=0;
	
    // Just the plain VB IMP
    imp.Select("//vb[@msd='imp']");
    //for (int impi=0;impi<imp.getLength();++impi)
    count += imp.getLength();

    // Currently TA often tags imperatives as infinitives instead, so we should 
    // include them too. (tag = “VB INF”).  But the rule is created so that the
    // preceding infinitive should not be a modal verb or infinitive in order to
    // exclude true infinitives from the count.

    imp.Select("//vb[@msd='inf' and not(preceding::text()/parent::vb[@msd='mod' or @msd='imp'])]");
	
    count += imp.getLength();

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),
                  "ImperativeVerbs",
                  count);
}


void Library::Adverbs(Context * context, LibraryOutput & output)
{
    if (output.Result("Adverbs")) return;

    XalanNavigator rb(context->Manager,context->Document);

    unsigned count=0;
	
    rb.Select("//rb");
    count += rb.getLength();


    output.Result(static_cast<int>(LibraryOutput::SUCCESS),
                  "Adverbs",
                  count);
}


void Library::AgentlessPassives(Context * context, LibraryOutput & output)
{
    if (output.Result("AgentlessPassives")) return;

    XalanNavigator pa(context->Manager,context->Document);

    unsigned count=0;
	
    // a) lemma =”be” + tag=”VB PCN ” + tok not ”by” 
    pa.Select("//text()/parent::node()[@base='be']/following::text()[1]/parent::vb[@msd='pcn']/following::text()[1][not(. = 'by')]");
    count += pa.getLength();

    // b) lemma =”be” + tag = “RB” | "QL" + tag=”VB PCN ” + tok not ”by” 
    pa.Select("//text()/parent::node()[@base='be']/following::text()[1]/parent::node()[self::rb or self::ql]/following::text()[1]/parent::vb[@msd='pcn']/following::text()[1][not(. = 'by')]");
    count += pa.getLength();

    // c) lemma =”be” + tag = “RB” | "QL" + tag=”RB” | "QL" + tag=”VB PCN ” + tok not ”by” 
    pa.Select("//text()/parent::node()[@base='be']/following::text()[1]/parent::node()[self::rb or self::ql]/following::text()[1]/parent::node()[self::rb or self::ql]/following::text()[1]/parent::vb[@msd='pcn']/following::text()[1][not(. = 'by')]");
    count += pa.getLength();

    // d) lemma = “be”+ tag=”<nps>” +tag=”VB PCN” + tok not ”by” 
    pa.Select("//text()/parent::node()[@base='be']/following::text()[1]/parent::nps/following::text()[1]/parent::vb[@msd='pcn']/following::text()[1][not(. = 'by')]");
    count += pa.getLength();


    output.Result(static_cast<int>(LibraryOutput::SUCCESS),
                  "AgentlessPassives",
                  count);
}

void Library::Amplifiers(Context * context, LibraryOutput & output)
{
    if (output.Result("Amplifiers")) return;

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

void Library::AnalyticNegation(Context * context, LibraryOutput & output)
{
    if (output.Result("AnalyticNegation")) return;

    XalanNavigator an(context->Manager,context->Document);

    unsigned count=0;	
    an.Select("//rb[@msd='neg'][@base='not']");
    count += an	.getLength();

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),
                  "AnalyticNegation",
                  count);
}

void Library::AttributiveAdjectives(Context * context, LibraryOutput & output)
{
    if (output.Result("AttributiveAdjectives")) return;

    XalanNavigator aa(context->Manager,context->Document);

    unsigned count=0;	

    // tag = “JJ” + tag = “JJ”|“NN”|”FW” 
    aa.Select("//jj/following::text()[1]/parent::node()[self::jj or self::nn or self::fw]");
    count += aa	.getLength();

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),
                  "AttributiveAdjectives",
                  count);
}


void Library::BeAsMainVerb(Context * context, LibraryOutput & output)
{
    if (output.Result("BeAsMainVerb")) return;

    Amplify::Module::List* possPro = List::Create("var/module/list/PossPro.txt");
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


void Library::ByPassives(Context * context, LibraryOutput & output)
{
    if (output.Result("ByPassives")) return;

    XalanNavigator pa(context->Manager,context->Document);

    unsigned count=0;
	

    // a) lemma =”be” + tag=”VB PCN ”+ tok=”by” 
    pa.Select("//text()/parent::node()[@base='be']/following::text()[1]/parent::vb[@msd='pcn']/following::text()[1][.='by' or .='BY' or .='By']");
    count += pa.getLength();

    // b) lemma =”be” + tag = “RB” | "QL" + tag=”VB PCN ”+ tok=”by” 
    pa.Select("//text()/parent::node()[@base='be']/following::text()[1]/parent::node()[self::rb or self::ql]/following::text()[1]/parent::vb[@msd='pcn']/following::text()[1][.='by' or .='BY' or .='By']");
    count += pa.getLength();

    // c) lemma =”be” + tag = “RB”|"QL" + tag=”RB”|"QL" + tag=”VB PCN”+ tok=”by” 
    pa.Select("//text()/parent::node()[@base='be']/following::text()[1]/parent::node()[self::rb or self::ql]/following::text()[1]/parent::node()[self::rb or self::ql]/following::text()[1]/parent::vb[@msd='pcn']/following::text()[1][.='by' or .='BY' or .='By']");
    count += pa.getLength();

    // d) lemma = “be”+ tag=”<nps>” +tag=”VB PCN”+ tok=”by” 
    pa.Select("//text()/parent::node()[@base='be']/following::text()[1]/parent::nps/following::text()[1]/parent::vb[@msd='pcn']/following::text()[1][.='by' or .='BY' or .='By']");
    count += pa.getLength();


    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"ByPassives",count);
}

void Library::CausativeSubordination(Context * context, LibraryOutput & output)
{
    if (output.Result("CausativeSubordination")) return;

    XalanNavigator an(context->Manager,context->Document);

    unsigned count=0;

    // tag = “CS” && tok = “because”|“since”|“for”|“as” 
    an.Select("//cs/text()[1][.='because' or .='since' or .='for' or .='as' or .='BECAUSE' or .='SINCE' or .='FOR' or .='AS' or .='Because' or .='Since' or .='for' or .='As']");
    count += an.getLength();

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),
                  "CausativeSubordination",
                  count);
}


void Library::Conjuncts(Context * context, LibraryOutput & output)
{
    if (output.Result("Conjuncts")) return;

    Amplify::Module::List* conjunctsList = List::Create("var/module/list/ConjunctsList.txt");

    Amplify::Module::List* inConjunctsList = List::Create("var/module/list/InConjunctList.txt");

    XalanNavigator co(context->Manager,context->Document);

    unsigned count=0;

    // a) lemma = ConjunctsList 
    co.Select("//text()/parent::node()/@base");
    count += static_cast<unsigned>(conjunctsList->Contains(co));

    // b) tok = “in” + tok = InConjunctList 
    co.Select("//text()[.='in' or .= 'IN' or .='In']/following::text()[1]");
    count += static_cast<unsigned>(inConjunctsList->Contains(co));

    // c) tok = “for” + tok = “example”|“instance” 
    co.Select("//text()[.='for' or .='For' or .='FOR']/following::text()[1][.='example' or .='instance' or .='Example' or .='Instance' or .='EXAMPLE' or .='INSTANCE']");
    count += co.getLength();

    // d) tok = “by” + tok = “comparison”|“contrast” 
    co.Select("//text()[.='by' or .='BY' or .='By']/following::text()[1][.='comparison' or .='contrast' or .='Comparison' or .='Contrast' or .='COMARISON' or .= 'CONTRAST']");
    count += co.getLength();

    // e) tok = “as” + tok = “a” + tok = “result”| “consequence” 
    co.Select("//text()[.='as' or .='AS' or .='As']/following::text()[1][.='a' or .= 'A']/following::text()[1][.='result' or .='Consequence' or .='Result' or .='Consequence' or .='RESULT' or .='CONSEQUENCE']");
    count += co.getLength();

    // f) tok = “on” + tok = “the” + tok = “contrary” 
    co.Select("//text()[.='on' or .= 'ON' or .='On']/following::text()[1][.='the' or .='The' or .='THE']/following::text()[1][.='contrary' or .='Contrary' or .='CONTRARY']");
    count += co.getLength();

    // g) tok = “on” + tok = “the” + tok = “other” + tok = “hand” 
    co.Select("//text()[.='on' or .= 'ON' or .='On']/following::text()[1][.='the' or .='The' or .='THE']/following::text()[1][.='other' or .='Other' or .='OTHER']/following::text()[1][.='hand' or .='Hand' or .='HAND']");
    count += co.getLength();

    // h) tok = "in" + tok = "other"|"any" + tok = "words"|"event"|"case"
    co.Select("//text()[.='in' or .= 'IN' or .='In']/following::text()[1][.='other' or .='Other' or .='OTHER' or .='any' or .='Any' or .='ANY']/following::text()[1][.='words' or .='event' or .='case' or .='Words' or .='Event' or .='Case' or .='WORDS' or .='EVENT' or .='CASE']");
    count += co.getLength();

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),
                  "Conjuncts",
                  count);
}



void Library::Contractions(Context * context, LibraryOutput & output)
{
    if (output.Result("Contractions")) return;

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

void Library::DemonstrativePronouns(Context * context, LibraryOutput & output)
{
    if (output.Result("DemonstrativePronouns")) return;

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

void Library::DiscourseParticles(Context * context, LibraryOutput & output)
{
    if (output.Result("DiscourseParticles")) return;

    XalanNavigator dp(context->Manager,context->Document);

    unsigned count=0;
	
    // tag=”<cl>” // tok = “well”|“now”|“anyway”|“anyhow”| “anyways” 
    // (first token in the clause is one of these) 
    dp.Select("//cl//text()[1][.='well' or .='now' or .='anyway' or .='anyhow' or .='anyways' or .='WELL' or .='NOW' or .='ANYWAY' or .='ANYHOW' or .='ANYWAYS' or .='Well' or .='Now' or .='Anyway' or .='Anyhow' or .='Anyways']");
    count += dp.getLength();

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"DiscourseParticles",count);
}


void Library::DoAsProVerb(Context * context, LibraryOutput & output)
{
    if (output.Result("DoAsProVerb")) return;

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


void Library::FirstPersonPronouns(Context * context, LibraryOutput & output)
{
    if (output.Result("FirstPersonPronouns")) return;

    XalanNavigator fp(context->Manager,context->Document);

    unsigned count=0;

    // tag = “PP” && lemma =”I”|“we”|“my”|“our”|“myself”| “ourselves” 
    fp.Select("//pp/@base[.='I' or .='we' or .='my' or .='our' or .='myself' or .='ourselves']");
    count += fp.getLength();

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"FirstPersonPronouns",count);
}

void Library::GeneralEmphatics(Context * context, LibraryOutput & output)
{
    if (output.Result("GeneralEmphatics")) return;

    XalanNavigator ge(context->Manager,context->Document);

    unsigned count=0;

    // a) tok=“for” + tok=”sure” 
    ge.Select("//text()[.='for' or .='For' or .='FOR']/following::text()[1][.='sure' or .='SURE' or .='Sure']");
    count += ge.getLength();

    // b) tok=”a” + tok=”lot” 
    ge.Select("//text()[.='a' or .='A']/following::text()[1][.='lot' or .='Lot' or .='LOT']");
    count += ge.getLength();

    // c) tok=”such” + tok=”a” 
    ge.Select("//text()[.='such' or .='SUCH' or .='Such']/following::text()[1][.='a' or .='A']");
    count += ge.getLength();

    // d) tok= “real”|”so” + (next token has) tag=”JJ” 
    ge.Select("//text()[.='real' or .='so' or .='Real' or .='So'or .='REAL' or .='SO']/following::text()[1]/parent::jj");
    count += ge.getLength();

    // e) lemma=”do” + (next token has) tag=”VB” 
    ge.Select("//text()[@base = 'do']/following::text()[1]/parent::vb");
    count += ge.getLength();

    // f) tok= ”just”|”really”|”more”|”most” 
    ge.Select("//text()[.='just' or .='really' or .='more' or .='most' or .='JUST' or .='REALLY' or .='MORE' or .='MOST' or .='Just' or .='Really' or .='More' or .='Most']");
    count += ge.getLength();

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"GeneralEmphatics",count);
}

void Library::GeneralHedges(Context * context, LibraryOutput & output)
{
    if (output.Result("GeneralHedges")) return;

    XalanNavigator gh(context->Manager,context->Document);

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

void Library::IndefinitePronouns(Context * context, LibraryOutput & output)
{
    if (output.Result("IndefinitePronouns")) return;

    XalanNavigator ip(context->Manager,context->Document);

    unsigned count=0;

    // tag = “PN NOM”|“PN GEN” 
    ip.Select("//pn[@msd = 'nom' or @msd ='gen']");
    count += ip.getLength();

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"IndefinitePronouns",count);;
}

void Library::Nouns(Context * context, LibraryOutput & output)
{
    if (output.Result("Nouns")) return;

    XalanNavigator nn(context->Manager,context->Document);

    unsigned count=0;

    // tag = “NN” 
    nn.Select("//nn");
    count += nn.getLength();

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"Nouns",count);
}

void Library::PastTenseVerbs(Context * context, LibraryOutput & output)
{
    if (output.Result("PastTenseVerbs")) return;

    XalanNavigator pt(context->Manager,context->Document);

    unsigned count=0;

    // tag = “VB PRT” | "VB PRT MOD" | "VB PRT SN3" | "VB PRT SY3"  
	
    pt.Select("//vb[(@msd = 'prt' or @msd = 'prt mod' or @msd = 'prt sn3' or @msd = 'prt sy3')]");

    count += pt.getLength();
	
    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"PastTenseVerbs",count);;	
}

void Library::PerfectAspect(Context * context, LibraryOutput & output)
{
    if (output.Result("PerfectAspect")) return;

    XalanNavigator pa(context->Manager,context->Document);

    unsigned count=0;

    // a) lemma = “have” + (next token has) tag=”VB PCN” 
    pa.Select("//@base[.='have']/parent::node()/following::text()[1]/parent::vb[@msd='pcn']");
    count += pa.getLength();

    // b) lemma = “have” + (next token has) tag=”RB” + (next token has) tag=”VB PCN” 
    pa.Select("//@base[.='have']/parent::node()/following::text()[1]/parent::rb/following::text()[1]/parent::vb[@msd='pcn']");
    count += pa.getLength();

    // c) lemma = “have” + (next token has)tag=”RB” + (next token has)tag=”RB” + (next token has) tag=”VB PCN”  (for statements) 
    pa.Select("//@base[.='have']/parent::node()/following::text()[1]/parent::node()[name() = 'rb' or name() = 'ql']/following::text()[1]/parent::rb/following::text()[1]/parent::vb[@msd='pcn']");
    count += pa.getLength();


    // d) lemma = “have” (followed by a closing tag </vs>) + tag=”<nps>...</nps>” + tag=”VB PCN”  (for questions) 
    pa.Select("//@base[.='have']/ancestor::vs/following::node()[1][self::nps]/following::text()[1]/parent::vb[@msd='pcn']");
    count += pa.getLength();

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"PerfectAspect",count);;
}


void Library::PlaceAdverbials(Context * context, LibraryOutput & output)
{
    if (output.Result("PlaceAdverbials")) return;

    Amplify::Module::List* placeAdvList = List::Create("var/module/list/PlaceAdvList.txt");
    //placeAdvList.Load("var/module/list/PlaceAdvList.txt");

    XalanNavigator pa(context->Manager,context->Document);

    unsigned count=0;

    // tag = “RB” | "NR" | "RN" && lemma = PlaceAdvList 
    pa.Select("//node()[self::rb or self::nr or self::rn]/@base");
    count += static_cast<unsigned>(placeAdvList->Contains(pa));

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"PlaceAdverbials",count);
}

void Library::Prepositions(Context * context, LibraryOutput & output)
{
    if (output.Result("Prepositions")) return;

    XalanNavigator pr(context->Manager,context->Document);

    unsigned count=0;

    // tag = “IN”
    pr.Select("//in");
    count += pr.getLength();

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"Prepositions",count);
}

void Library::PresentTenseVerbs(Context * context, LibraryOutput & output)
{
    if (output.Result("PresentTenseVerbs")) return;

    XalanNavigator pr(context->Manager,context->Document);

    unsigned count=0;

    // tag = “VB PRS SN3” | "VB PRS MOD" | "VB PRS SY1" | "VB PRS SY3"
    pr.Select("//vb[(@msd = 'prs sn3' or @msd = 'prs sy3' or @msd = 'prs mod' or @msd = 'prs sy1')]");

    count += pr.getLength();
	
    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"PresentTenseVerbs",count);
}

void Library::PrivateVerbs(Context * context, LibraryOutput & output)
{
    if (output.Result("PrivateVerbs")) return;

    Amplify::Module::List* privateVerbsList = List::Create("var/module/list/PrivateVerbsList.txt");
    //privateVerbsList.Load("var/module/list/PrivateVerbsList.txt");

    XalanNavigator pv(context->Manager,context->Document);

    unsigned count=0;

    // tag = “VB” && lemma = PrivateVerbsList  
    pv.Select("//vb/@base");
    count += static_cast<unsigned>(privateVerbsList->Contains(pv));

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"PrivateVerbs",count);
}

void Library::PronounIt(Context * context, LibraryOutput & output)
{
    if (output.Result("PronounIt")) return;

    XalanNavigator pr(context->Manager,context->Document);

    unsigned count=0;

    // tag = “PP” && lemma = “it” 
    pr.Select("//pp[@base='it']");
    count += pr.getLength();

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"PronounIt",count);
}

void Library::PublicVerbs(Context * context, LibraryOutput & output)
{
    if (output.Result("PublicVerbs")) return;

    Amplify::Module::List* publicVerbsList = List::Create("var/module/list/PublicVerbsList.txt");
    //publicVerbsList.Load("var/module/list/PublicVerbsList.txt");

    XalanNavigator pv(context->Manager,context->Document);

    unsigned count=0;

    // tag = “VB” && lemma = PublicVerbsList  
    pv.Select("//vb/@base");
    count += static_cast<unsigned>(publicVerbsList->Contains(pv));

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"PublicVerbs",count);
}

void Library::SecondPersonPronouns(Context * context, LibraryOutput & output)
{
    if (output.Result("SecondPersonPronouns")) return;

    XalanNavigator sp(context->Manager,context->Document);

    unsigned count=0;

    // tag = “PP” && lemma = “you”|“your”|“yourself”| “yourselves” 
    sp.Select("//pp[@base='you' or @base='your' or @base='yourself' or @base='yourselves']");
    count += sp.getLength();

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"SecondPersonPronouns",count);
}

void Library::ThirdPersonPronouns(Context * context, LibraryOutput & output)
{
    if (output.Result("ThirdPersonPronouns")) return;

    XalanNavigator sp(context->Manager,context->Document);

    unsigned count=0;

    // tag = “PP” && lemma = “she”|“he”|“they”|“her”| “him”|“them”|“his”|“their”|“himself”|“herself”| “themselves” 
    sp.Select("//pp[@base='she' or @base='he' or @base='they' or @base='her' or @base='him' or @base='them' or @base='his' or @base='their' or @base='himself' or @base='herself' or @base='themselves']");
    count += sp.getLength();

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"ThirdPersonPronouns",count);
}

void Library::TimeAdverbials(Context * context, LibraryOutput & output)
{
    if (output.Result("TimeAdverbials")) return;

    Amplify::Module::List* timeAdvList = List::Create("var/module/list/TimeAdvList.txt");
    //timeAdvList.Load("var/module/list/TimeAdvList.txt");

    XalanNavigator ta(context->Manager,context->Document);

    unsigned count=0;

    // tag = “RB" | "NR" && lemma = TimeAdvList   
    ta.Select("//node()[self::rb or self::nr]/@base");
    count += static_cast<unsigned>(timeAdvList->Contains(ta));

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"TimeAdverbials",count);
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

void Library::SplitAuxiliaries(Context * context, LibraryOutput & output)
{
    if (output.Result("SplitAuxiliaries")) return;

    XalanNavigator sa(context->Manager,context->Document);

    unsigned count=0;
	
    // a) tag=”VB” + tag=“RB” | "QL" + tag=“VB” 
    sa.Select("//vb/following::text()[1]/parent::node()[self::rb or self::ql]/following::text()[1]/parent::vb");
    count += sa.getLength();

    // b) tag=”VB” + tag=“RB” | "QL" + tag=“RB” | "QL" + tag=“VB” 
    sa.Select("//vb/following::text()[1]/parent::node()[self::rb or self::ql]/following::text()[1]/parent::node()[self::rb or self::ql]/following::text()[1]/parent::vb");
    count += sa.getLength();

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"SplitAuxiliaries",count);
}

void Library::ConditionalSubordination(Context * context, LibraryOutput & output)
{
    if (output.Result("ConditionalSubordination")) return;

    XalanNavigator cs(context->Manager,context->Document);

    unsigned count=0;
    std::string dbgStr;
    // tag = “CS” && tok = “if”|“unless” 
    //cs.Select("//cs/text()[1][.='if' or .='unless']");
    cs.Select("//cs/text()");
	
    for (unsigned i= 0; i < cs.getLength();i++ )
	{	
            XalanNode *node = cs.item(i);
            dbgStr.clear();
            XalanLibrary::Print(node, dbgStr);
	    Lowercase(dbgStr);
            //std::transform(dbgStr.begin(),dbgStr.end(),dbgStr.begin(),tolower);
            if(dbgStr == "if" || dbgStr == "unless")
                count++;

	}
	

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"ConditionalSubordination",count);	
}

void Library::Infinitives(Context * context, LibraryOutput & output)
{
    if (output.Result("Infinitives")) return;

    XalanNavigator in(context->Manager,context->Document);

    unsigned count=0;
	
    // a) tok = "to" + tag = “VB INF” 
    in.Select("//text()[.='to' or .='To' or .='TO']/following::text()[1]/parent::vb[@msd = 'inf']");
    count += in.getLength();

    // b) tok = "to" + tag = "RB" + tag = “VB INF” 
    in.Select("//text()[.='to' or .='To' or .='TO']/following::text()[1]/parent::rb/following::text()[1]/parent::vb[@msd = 'inf']");
    count += in.getLength();

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"Infinitives",count);	
}

void Library::NecessityModals(Context * context, LibraryOutput & output)
{
    if (output.Result("NecessityModals")) return;


    XalanNavigator nm(context->Manager,context->Document);
    XalanNavigator attr(context->Manager,context->Document);
    unsigned count=0;

    // tag = “VB.*MOD” && tok=”ought”|“should”|“must” 

    attr.Select("//vb/text()[.='ought' or .='should' or .='must' or .='Ought' or .='Should' or .='Must' or .='OUGHT' or .='SHOULD' or .='MUST']");
    for (unsigned j=0;j<attr.getLength();j++)
	{
            nm.Select(attr.item(j)->getParentNode(),"./@msd");
            for (unsigned nmi=0;nmi<nm.getLength();++nmi)
		{
                    if (XalanLibrary::Matches(nm.item(nmi),"* mod"))
                        ++count;
		}
	}

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"NecessityModals",count);	
}



void Library::PossibilityModals(Context * context, LibraryOutput & output)
{
    if (output.Result("PossibilityModals")) return;

    XalanNavigator pm(context->Manager,context->Document);

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

void Library::PredictionModals(Context * context, LibraryOutput & output)
{
    if (output.Result("PredictionModals")) return;

    XalanNavigator tok(context->Manager,context->Document);
    XalanNavigator pm(context->Manager,context->Document);

    unsigned count=0;

    // tag = .VB.*MOD. && tok=.will.|.would.|.shall. 
	
    tok.Select("//vb//text()");
    for (unsigned ntok=0;ntok<tok.getLength();++ntok)
	{
            std::string word;
            XalanNode * txt = tok.item(ntok);
            XalanLibrary::ToString(txt->getNodeValue(),word);
            Lowercase(word);
            if(word == "will" || word == "shall" || word == "would")
		{
                    pm.Select(tok.item(ntok)->getParentNode(),"./@msd");
                    for (unsigned pmi=0;pmi<pm.getLength();++pmi)
			{
                            if (XalanLibrary::Matches(pm.item(pmi),"* mod"))
				{
                                    ++count;
				}
			}
		}
	}
    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"PredictionModals",count);	
}


void Library::SuasiveVerbs(Context * context, LibraryOutput & output)
{
    if (output.Result("SuasiveVerbs")) return;

    Amplify::Module::List* suasiveVerbsList = List::Create("var/module/list/SuasiveVerbsList.txt");
    //suasiveVerbsList.Load("var/module/list/SuasiveVerbsList.txt");


    XalanNavigator sv(context->Manager,context->Document);

    unsigned count=0;

    // tag = “VB.*” && lemma = SuasiveVerbsList  

    sv.Select("//vb/@base");

    for (unsigned svi=0;svi<sv.getLength();++svi)
	{
            XalanNode * attribute = sv.item(svi);
            if (attribute && suasiveVerbsList->Contains(attribute))
		{
                    ++count;
		}
	}

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"SuasiveVerbs",count);	
}


void Library::ContrastiveDiscourseConnectives(Context * context, LibraryOutput & output)
{
    if (output.Result("ContrastiveDiscourseConnectives")) return;

    Amplify::Module::List* oneWordCdcList = List::Create("var/module/list/OneWordCdc.txt");
    //oneWordCdcList.Load("var/module/list/OneWordCdc.txt");

    Amplify::Module::List* multiWordCdcList = List::Create("var/module/list/MultiWordCdc.txt");
    //multiWordCdcList.Load("var/module/list/MultiWordCdc.txt");
    std::string tmpfilename = Resource::QualifyPath("var/module/list/MultiWordCdc.txt");
    std::set<std::string>listSet = multiWordCdcList->getListSet(tmpfilename);
    XalanNavigator cd(context->Manager,context->Document);

    unsigned count=0;
    unsigned multi_count=0;
    // 1. token = OneWordCdc List 
    cd.Select("//text()");
    for (unsigned cdi=0;cdi<cd.getLength();++cdi)
	{
            if (oneWordCdcList->Contains(cd.item(cdi)))
		{
                    ++count;
		}
	}
	
    multi_count += static_cast<unsigned>(multiWordCdcList->MutliWordCount(listSet, context->Text));
    count +=multi_count;


    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"ContrastiveDiscourseConnectives",count);	
}


void Library::SubjectiveVerbs(Context * context, LibraryOutput & output)
{
    if (output.Result("SubjectiveVerbs")) return;

    Amplify::Module::List* subjectiveVerbsList = List::Create("var/module/list/SubjectiveVerbs.txt");
    //subjectiveVerbsList.Load("var/module/list/SubjectiveVerbs.txt");


    XalanNavigator sv(context->Manager,context->Document);

    unsigned count=0;

    // lemma = SubjectiveVerbsList 

    sv.Select("//text()/parent::node()/@base");

    for (unsigned svi=0;svi<sv.getLength();++svi)
	{
            XalanNode * attribute = sv.item(svi);
            if (attribute && subjectiveVerbsList->Contains(attribute))
		{
                    ++count;
		}
	}

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"SubjectiveVerbs",count);	
}



void Library::CertaintyAdverbs(Context * context, LibraryOutput & output)
{
    if (output.Result("CertaintyAdverbs")) return;

    Amplify::Module::List* certaintyAdverbsList = List::Create("var/module/list/CertaintyAdverbsList.txt");
    //certaintyAdverbsList.Load("var/module/list/CertaintyAdverbsList.txt");

    Amplify::Module::List* certaintyMulti = List::Create("var/module/list/CertaintyMulti.txt");
    std::string tmpfilename = Resource::QualifyPath("var/module/list/CertaintyMulti.txt");
    std::set<std::string>listSet = certaintyMulti->getListSet(tmpfilename);
    XalanNavigator ca(context->Manager,context->Document);

    unsigned count=0;
	
    ca.Select("//rb//text()");
    for (unsigned cai=0;cai<ca.getLength();++cai)
	{
            // tag = “RB” && token = CertaintyAdverbsList 
            XalanNode * attribute = ca.item(cai);
            if (attribute && certaintyAdverbsList->Contains(attribute))
		{
                    ++count;
		}

	}
    unsigned multi_count = static_cast<unsigned>(certaintyMulti->MutliWordCount(listSet,context->Text));
    count +=multi_count;
    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"CertaintyAdverbs",count);	
}



void Library::DecisivenessMarkers(Context * context, LibraryOutput & output)
{

    if (output.Result("DecisivenessMarkers")) return;

    XalanNavigator dm(context->Manager,context->Document);

    unsigned count=0;
	
    // token = "going" | "gon" | "got" | "haf" | "have" | "need" + (next token has) lemma = "to"
    dm.Select("//text()[.='going' or .='gon' or .='got' or .='haf' or .='have' or .='Going' or .='Gon' or .='Got' or .='Haf' or .='Have' or .='GOING' or .='GON' or .='GOT' or .='HAF' or .='HAVE' or ((.='need' or .='Need' or .='NEED') and following::text()[1]/parent::node()[@base = 'to'])]");
    count += dm.getLength();

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"DecisivenessMarkers",count);	
}

void Library::DefiniteArticles(Context * context, LibraryOutput & output)
{
    if (output.Result("DefiniteArticles")) return;

    XalanNavigator da(context->Manager,context->Document);

    unsigned count=0;
	
    // The definite articles to look for and count per sentence is the words with 
    // lemmas: “the”, “that”, “these”, “those”, when occurring as the initial word in a <np.*>. 

    // tag = <np> | <npo> | <nps> | <npx> / first child token has lemma = "the" | "that" | "these" | "those" 

    da.Select("//node()[self::np or self::npo or self::nps or self::npx]//text()[1]/parent::node()/@base[.='the' or .='that' or .='these' or .='those']");
    count += da.getLength();

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"DefiniteArticles",count);	
}

void Library::IndefiniteArticles(Context * context, LibraryOutput & output)
{
    if (output.Result("IndefiniteArticles")) return;

    XalanNavigator ia(context->Manager,context->Document);

    unsigned count=0;

    // The indefinite articles to look for and count per sentence are the tokens: “a”, “an”, 
    // when occurring as the initial word in a <np.*>. 

    // tag = <np> | <npo> | <nps> | <npx> / first child token = "a" | "an" 


    ia.Select("//node()[self::np or self::npo or self::nps or self::npx]//text()[1][.='a' or .='an' or .='A' or .='AN' or .='An']");
    count += ia.getLength();

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"IndefiniteArticles",count);	
}

void Library::NPs(Context * context, LibraryOutput & output)
{
    if (output.Result("NPs")) return;

    XalanNavigator np(context->Manager,context->Document);

    unsigned count=0;

    // Count number of noun phrases: <np> <nps> <npo> <npx>

    np.Select("//node()[self::np or self::npo or self::nps or self::npx]");
    count += np.getLength();

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"NPs",count);	
}



void Library::NumberOfTypes(Context * context, LibraryOutput & output)
{
    if (output.Result("NumberOfTypes")) return;

    XalanNavigator nt(context->Manager,context->Document);

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

void Library::NumberOfWords(Context * context, LibraryOutput & output)
{
    if (output.Result("NumberOfWords")) return;

    XalanNavigator nt(context->Manager,context->Document);

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

void Library::Pronouns(Context * context, LibraryOutput & output)
{
    if (output.Result("Pronouns")) return;

    XalanNavigator pn(context->Manager,context->Document);

    unsigned count=0;

    // count all tags = "DT" | "PN" | "PP" 

    pn.Select("//node()[name()= 'dt' or name() = 'pn']");
    count += pn.getLength();
    pn.Select("//pp[@msd]");
    count += pn.getLength();
	
    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"Pronouns",count);	
}


void Library::MeanDefiniteArticles(Context * context, LibraryOutput & output)
{
    if (output.Result("MeanDefiniteArticles")) return;

    XalanNavigator md(context->Manager,context->Document);

    unsigned count=0;

    // The ratio DefiniteArticles / tag=<<s>> 

    int countDefiniteArticles = 0;
    Execute(context,output,"DefiniteArticles",countDefiniteArticles);
	
    md.Select("//s");
    count += md.getLength();

    double ratio;
    if(count == 0)
        ratio = 0;
    else
        ratio = (double)countDefiniteArticles / (double)count;

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"MeanDefiniteArticles",ratio);	
}



void Library::DefiniteIndefiniteRatio(Context * context, LibraryOutput & output)
{
    if (output.Result("DefiniteIndefiniteRatio")) return;

    // Ratio DefiniteArticles / IndefiniteArticles 

    int countDefiniteArticles = 0;
    Execute(context,output,"DefiniteArticles",countDefiniteArticles);

    int countIndefiniteArticles = 0;
    Execute(context,output,"IndefiniteArticles",countIndefiniteArticles);

    double ratio;
    if(countIndefiniteArticles == 0)
        ratio = 0;
    else
        ratio = (double)countDefiniteArticles / (double)countIndefiniteArticles;

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"DefiniteIndefiniteRatio",ratio);	
}



void Library::PronounsNpRatio(Context * context, LibraryOutput & output)
{
    if (output.Result("PronounsNpRatio")) return;

    // Ratio Pronouns / NPs 

    int countPronouns = 0;
    Execute(context,output,"Pronouns",countPronouns);

    int countNPs = 0;
    Execute(context,output,"NPs",countNPs);

    double ratio;
    if(countNPs == 0)
        ratio = 0;
    else
        ratio =(double)countPronouns / (double)countNPs;

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"PronounsNpRatio",ratio);	
}


void Library::NounsNpRatio(Context * context, LibraryOutput & output)
{
    if (output.Result("NounsNpRatio")) return;

    XalanNavigator nn(context->Manager,context->Document);

    // Return the ratio nouns (tag = "NN" |"PM" ) / NPs  

    unsigned count=0;


    nn.Select("//node()[self::nn or self::pm]");
    count += nn.getLength();


    int countNPs = 0;
    Execute(context,output,"NPs",countNPs);
    double ratio;
    if(countNPs == 0)
        ratio = 0;
    else
        ratio = (double)count / (double)countNPs;

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"NounsNpRatio",ratio);	
}


void Library::ModifiersNPsRatio(Context * context, LibraryOutput & output)
{
    if (output.Result("ModifiersNPsRatio")) return;

    // Ratio ModifierWords / NPs 

    int countModifierWords = 0;
    Execute(context,output,"ModifierWords",countModifierWords);

    int countNPs = 0;
    Execute(context,output,"NPs",countNPs);
    double ratio;
    if(countNPs == 0)
        ratio = 0;
    else
        ratio = (double)countModifierWords / (double)countNPs;

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"ModifiersNPsRatio",ratio);	
}


void Library::MeanSyllables(Context * context, LibraryOutput & output)
{
    if (output.Result("MeanSyllables")) return;

    int numberOfSyllables = 0, numberOfWords = 0;
    double meanSyllables = 0;
	
    Execute(context,output,"NumberOfSyllables",numberOfSyllables);
    Execute(context,output,"NumberOfWords",numberOfWords);
	
	
    if(numberOfWords == 0)
        meanSyllables = 0;
    else
        meanSyllables = (double)numberOfSyllables / (double)numberOfWords;


    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"MeanSyllables",meanSyllables);	
}



void Library::MeanPrepositionalPhrases(Context * context, LibraryOutput & output)
{
    if (output.Result("MeanPrepositionalPhrases")) return;

    XalanNavigator mpp(context->Manager,context->Document);

    // The average number of prepositional phrases per sentence. 
    // count all <<pp>> and <<s>> in the text, return the ratio <<pp>> / <<s>> 
	
    mpp.Select("//pp");
    unsigned temp = mpp.getLength();
	
    mpp.Select("//pp[@msd]");
    unsigned ppCount = temp - mpp.getLength();

    mpp.Select("//s");
    unsigned sCount = mpp.getLength();
    double mean;
    if(sCount == 0)
        mean = 0;
    else
        mean =(double)ppCount / (double)sCount;

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"MeanPrepositionalPhrases",mean);	
}




void Library::MeanSyntacticDepth(Context * context, LibraryOutput & output)
{
    if (output.Result("MeanSyntacticDepth")) return;

    XalanNavigator mpp(context->Manager,context->Document);

    // The maximum depth of every sentence, calculated as an average throughout all sentences 
    // in the text. Complex sentences are less readable than simple sentences and the proportion 
    // of complex sentences in a text is often used as a feature in traditional readability formulas. 
    // The TA output to use to calculate this will be the average number of begin clause tags
    // <<cl>> per sentence <<s>>. 
	
    // Count all <<cl>> and <<s>>, return the ratio <<cl>> / <<s>> 

    mpp.Select("//cl");
    unsigned clCount = mpp.getLength();
	
    mpp.Select("//s");
    unsigned sCount = mpp.getLength();
    double mean;
    if(sCount == 0)
        mean = 0;
    else
        mean = (double)clCount / (double)sCount;

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"MeanSyntacticDepth",mean);	
}




void Library::TypeTokenRatio(Context * context, LibraryOutput & output)
{
    if (output.Result("TypeTokenRatio")) return;

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




void Library::MeanWordLength(Context * context, LibraryOutput & output)
{
    if (output.Result("MeanWordLength")) return;

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

void Library::MeanSentenceLength(Context * context, LibraryOutput & output)
{
    if (output.Result("MeanSentenceLength")) return;

    XalanNavigator msl(context->Manager,context->Document);

    unsigned count=0;

    // Mean sentence length: ratio NumberOfWords / tag = <<s>> 
    // (Number of words (tokens) in the text divided by the total number of sentences (<<s>>). ) 

    msl.Select("//s");
    count += msl.getLength();
	
    int numberOfWords = 0;
	
    Execute(context,output,"NumberOfWords",numberOfWords);
    double mean;
    if(count == 0)
        mean = 0;
    else
        mean =   (double)numberOfWords / (double)count;
	
    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"MeanSentenceLength",mean);	
}

void Library::EmotionWords(Context * context, LibraryOutput & output)
{
    if (output.Result("EmotionWords")) return;

    Amplify::Module::List* emotionWordList = List::Create("var/module/list/EmotionWordList.txt");

    XalanNavigator ew(context->Manager,context->Document);

    unsigned count=0;
	
    // lemma = EmotionWordList
    ew.Select("//rb/@base");
    for (unsigned ewi=0;ewi<ew.getLength();++ewi)
	{
            XalanNode * attribute = ew.item(ewi);
            if (attribute && emotionWordList->Contains(attribute))
		{
                    ++count;
		}
	}

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"EmotionWords",count);	
}

void Library::FlamboyanceWords(Context * context, LibraryOutput & output)
{
    if (output.Result("FlamboyanceWords")) return;

    Amplify::Module::List* flamboyanceList = List::Create("var/module/list/FlamboyanceList.txt");

    XalanNavigator fw(context->Manager,context->Document);

    unsigned count=0;

    // a) Count all JJ where lemma = FlamboyanceList (tag = "JJ" && lemma = FlamboyanceList) 
    // b) Count all RB where lemma = FlamboyanceList (tag = "RB" && lemma = FlamboyanceList)
    // c) Count all RB where the token ends with the string “ly” and the string preceding “ly” is in FlamboyanceList.
    // d) sum up the counts a + b + c 

    fw.Select("//jj/@base");
    for (unsigned fwi=0;fwi<fw.getLength();++fwi)
	{
            XalanNode * attribute = fw.item(fwi);
            if (attribute && flamboyanceList->Contains(attribute))
                ++count;
	}


    fw.Select("//rb/@base");
    for (unsigned fwi=0;fwi<fw.getLength();++fwi)
	{
            XalanNode * attribute = fw.item(fwi);
            if (attribute && flamboyanceList->Contains(attribute))
                ++count;
	}

    fw.Select("//rb/text()[1]");
    for (unsigned fwi=0;fwi<fw.getLength();++fwi)
	{
            XalanNode * rb = fw.item(fwi);
            if (rb)
		{
                    std::string word;
                    XalanLibrary::ToString(rb->getNodeValue(),word);
                    if (word.find("ly") == word.size()-2)
			{
                            word = word.substr(0,word.size()-2);
                            if (flamboyanceList->Contains(word))
                                ++count;
			}
		}
	}


    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"FlamboyanceWords",count);	
}


void Library::LongAdjectivesOrAdverbs(Context * context, LibraryOutput & output)
{
    if (output.Result("LongAdjectivesOrAdverbs")) return;

    XalanNavigator la(context->Manager,context->Document);

    unsigned count=0;
	
    // a) Find all tag = “JJ” whose token equals or is longer than 8 letters.
    // b) Find all tag = “RB” whose token equals or is longer than 10 letters.

    la.Select("//jj/text()[1][string-length() > 8]");
    count += la.getLength();


    la.Select("//rb/text()[1][string-length() > 10]");
    count += la.getLength();

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"LongAdjectivesOrAdverbs",count);	
}


void Library::ModifierWords(Context * context, LibraryOutput & output)
{
    if (output.Result("ModifierWords")) return;

    XalanNavigator mw(context->Manager,context->Document);

    unsigned count=0;
	
    // Attributes/Modifiers: 
    // tag = “JJ” | “VB PCN” | “VB PCG” | “RB” | “QL” 

    mw.Select("//jj | //vb[@msd = 'pcn' or @msd = 'pcg'] | //rb | //ql");
    count += mw.getLength();

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"ModifierWords",count);	
}


void Library::FemaleWords(Context * context, LibraryOutput & output)
{
    if (output.Result("FemaleWords")) return;

    Amplify::Module::List* femaleList = List::Create("var/module/list/FemaleList.txt");

    XalanNavigator fw(context->Manager,context->Document);

    unsigned count=0;
	
    // lemma = FemaleList? 
    // (token sequence = FemaleList? - for multi-word units) 

    // FIXME
    // Multi word lists are not supported

    fw.Select("//text()");
    for (unsigned fwi=0;fwi<fw.getLength();++fwi)
	{
            if (femaleList->Contains(fw.item(fwi)))
                ++count;
	}

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"FemaleWords",count);	
}



void Library::MaleWords(Context * context, LibraryOutput & output)
{
    if (output.Result("MaleWords")) return;

    Amplify::Module::List* maleList = List::Create("var/module/list/MaleList.txt");

    XalanNavigator mw(context->Manager,context->Document);

    unsigned count=0;
	
    // lemma = MaleList? 
    // (token sequence = MaleList? - for multi-word units) 

    // FIXME
    // Multi word lists are not supported

    mw.Select("//text()");
    for (unsigned mwi=0;mwi<mw.getLength();++mwi)
	{
            if (maleList->Contains(mw.item(mwi)))
                ++count;
	}

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"MaleWords",count);	
}



void Library::CommonWords(Context * context, LibraryOutput & output)
{
    if (output.Result("CommonWords")) return;

    Amplify::Module::List* commonWordsList = List::Create("var/module/list/CommonWordsList.txt");

    XalanNavigator cw(context->Manager,context->Document);

    unsigned count=0;

    // token = CommonWordsList 

    cw.Select("//text()");
    for (unsigned cwi=0;cwi<cw.getLength();++cwi)
	{
            if (commonWordsList->Contains(cw.item(cwi)))
                ++count;
	}

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"CommonWords",count);	
}



void Library::PunctuationMarks(Context * context, LibraryOutput & output)
{
    if (output.Result("PunctuationMarks")) return;

    Amplify::Module::List* punctuationList = List::Create("var/module/list/PunctuationList.txt");

    XalanNavigator pm(context->Manager,context->Document);

    unsigned count=0;

    // token = PunctuationList  

    pm.Select("//text()");
    for (unsigned pmi=0;pmi<pm.getLength();++pmi)
	{
            if (punctuationList->Contains(pm.item(pmi)))
                ++count;
	}

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"PunctuationMarks",count);	
}




void Library::SlangWords(Context * context, LibraryOutput & output)
{
    if (output.Result("SlangWords")) return;

    Amplify::Module::List* slangList = List::Create("var/module/list/SlangList.txt");

    XalanNavigator sw(context->Manager,context->Document);
    XalanNavigator swa(context->Manager,context->Document);

    unsigned count=0;

    // token = SlangList or lemma = SlangList 

    sw.Select("//text()");
    for (unsigned swi=0;swi<sw.getLength();++swi)
	{
            XalanNode * node = sw.item(swi);
            XalanNode * attribute = swa.Evaluate(node,"./parent::node()/@base");
            if (slangList->Contains(node) || slangList->Contains(attribute))
                ++count;
	}

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"SlangWords",count);	
}



void Library::YesNoQuestions(Context * context, LibraryOutput & output)
{
    if (output.Result("YesNoQuestions")) return;

    XalanNavigator yes_no_navigator(context->Manager,context->Document);

    unsigned count=0;

    // Return the count of the following phrases: 
		

    std::string xpath;
    std::string whatWhenWhoWhichWhere = ".='what' or .='when' or .='who' or .='which' or .='where' or .='What' or .='When' or .='Who' or .='Which' or .='Where' or .='WHAT' or .='WHEN' or .='WHO' or .='WHICH' or .='WHERE'";

    // "what|when|who|which|where is the" + next token has a tag = “JJ SUV” (adjective in the superlative form, e.g. "When is the best time to go to Florida?")
    xpath = "//text()["+ whatWhenWhoWhichWhere +"]/following::text()[1][.='is' or .='Is' or .='IS']/following::text()[1][.='the' or .='The' or .='THE']/following::text()[1]/parent::jj[@msd='suv']";
    yes_no_navigator.Select(xpath.c_str());
    count += yes_no_navigator.getLength();

    // "what|when|who|which|where is the" + next token = "most" ("What is the most suitable type of fire extinguisher to use on an oil fire?")
    xpath = "//text()["+ whatWhenWhoWhichWhere +"]/following::text()[1][.='is' or .='Is' or .='IS']/following::text()[1][.='the' or .='The' or .='THE']/following::text()[1][.='most']";
    yes_no_navigator.Select(xpath.c_str());
    count += yes_no_navigator.getLength();

    // "is it better to"
    yes_no_navigator.Select("//text()[.='is' or .='Is' or .='IS']/following::text()[1][.='it' or .='It' or .='IT']/following::text()[1][.='better' or .='BETTER' or .='Better']/following::text()[1][.='to' or .='TO' or .='To']");
    count += yes_no_navigator.getLength();

    // "should I"
    yes_no_navigator.Select("//text()[.='should' or .='Should' or .='SHOULD']/following::text()[1][.='I' or .='i']");
    count += yes_no_navigator.getLength();

    // "what|when|who|which|where|how" + next token has a tag = "VB.*MOD" + next token = "I"|"we"|"you" + next token has a tag = "VB"
    xpath = "//text()["+ whatWhenWhoWhichWhere +" or .='how' or .='How' or .='HOW']/following::text()[1]/parent::vb/@msd";
    yes_no_navigator.Select(xpath.c_str());
    for (unsigned yni=0;yni<yes_no_navigator.getLength();++yni)
	{
            XalanNode * attribute = yes_no_navigator.item(yni);
            if (attribute && XalanLibrary::Matches(attribute,"* mod"))
		{
                    XalanNode * node = yes_no_navigator.Evaluate(attribute,"parent::node()/following::text()[1][.='I' or .='i' or .='we' or .='you' or .='We' or .='You' or .='WE' or .='YOU']/following::text()[1]/parent::vb");
                    if (node)
                        ++count;
		}
	}

    // "how much|many|often" + next token has a tag = "VB.*MOD" + next token = "I"|"we"|"you" + next token has a tag = "VB"
    xpath = "//text()[.='how' or .='HOW' or .='How']/following::text()[1][.='much' or .='many' or .='often'or .='Much' or .='Many' or .='Often' or .='MUCH' or .='MANY' or .='OFTEN']/following::text()[1]/parent::vb/@msd";
    yes_no_navigator.Select(xpath.c_str());
    for (unsigned yni=0;yni<yes_no_navigator.getLength();++yni)
	{
            XalanNode * attribute = yes_no_navigator.item(yni);
            if (attribute && XalanLibrary::Matches(attribute,"* mod"))
		{
                    XalanNode * node = yes_no_navigator.Evaluate(attribute,"parent::node()/following::text()[1][.='I' or .='we' or .='you']/following::text()[1]/parent::vb");
                    if (node)
                        ++count;
		}
	}


    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"YesNoQuestions",count);	
}


void Library::OfferingGuidancePhrases(Context * context, LibraryOutput & output)
{
	
    if (output.Result("OfferingGuidancePhrases")) return;

    Amplify::Module::List* offeringGuidanceeList = List::Create("var/module/list/OfferingGuidanceList.txt");
    std::string tmpfilename = Resource::QualifyPath("var/module/list/OfferingGuidanceList.txt");
    std::set<std::string>listSet = offeringGuidanceeList->getListSet(tmpfilename);
    unsigned count = static_cast<unsigned>(offeringGuidanceeList->MutliWordCount(listSet,context->Text));
		
    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"OfferingGuidancePhrases",count);	
}


void Library::RequestingGuidancePhrases(Context * context, LibraryOutput & output)
{
    if (output.Result("RequestingGuidancePhrases")) return;

    Amplify::Module::List* requestingGuidanceList = List::Create("var/module/list/RequestingGuidanceList.txt");
    std::string tmpfilename = Resource::QualifyPath("var/module/list/RequestingGuidanceList.txt");
    std::set<std::string>listSet = requestingGuidanceList->getListSet(tmpfilename);

    unsigned count = static_cast<unsigned>(requestingGuidanceList->MutliWordCount(listSet,context->Text));

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"RequestingGuidancePhrases",count);	
}




void Library::ExplicitTemporalReferences(Context * context, LibraryOutput & output)
{
    if (output.Result("ExplicitTemporalReferences")) return;

    // Extract phrases enclosed within tag = “<<tp>>”
    // lemma = EventsList
    // token sequence = EventsListMulti
    // Return the sum of all the above. 

    int temporalCount = 0;
    Execute(context,output,"TemporalPhrases",temporalCount);

    int eventCount = 0;
    Execute(context,output,"Events",eventCount);
	
    unsigned count = static_cast<unsigned>(temporalCount + eventCount);

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"ExplicitTemporalReferences",count);	
}


void Library::IntentionVerbs(Context * context, LibraryOutput & output)
{
    if (output.Result("IntentionVerbs")) return;

    XalanNavigator iv(context->Manager,context->Document);

    unsigned count=0;
	
    // Intention verbs (also used as Future temporal reference) 
    // Verb groups containing the intention verbs aim, intend, mean, plan, will. 
    // tag = “VB PRS.*” | “VB PCG” && lemma = “aim” | “intend”| “mean” | “plan” | “will” 


    iv.Select("//vb/@msd");
    for (unsigned ivi=0;ivi<iv.getLength();++ivi)
	{
            XalanNode * attribute = iv.item(ivi);
            if (attribute && XalanLibrary::Matches(attribute,"prs *"))
                ++count;
	}
	
    iv.Select("//vb[@msd='pcg']/@base[.='aim' or .='intend' or .='mean' or .='plan' or .='will']");
    count += iv.getLength();

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"IntentionVerbs",count);
}


void Library::BiberFactor1(Context * context, LibraryOutput & output)
{
    if (output.Result("BiberFactor1")) return;

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
    Execute(context,output,"DoAsProVerb",positive);
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

void Library::BiberFactor2(Context * context, LibraryOutput & output)
{
    if (output.Result("BiberFactor2")) return;
	

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

void Library::BiberFactor3(Context * context, LibraryOutput & output)
{
    if (output.Result("BiberFactor3")) return;

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

void Library::BiberFactor4(Context * context, LibraryOutput & output)
{
    if (output.Result("BiberFactor4")) return;

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

void Library::BiberFactor5(Context * context, LibraryOutput & output)
{
    if (output.Result("BiberFactor5")) return;

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

void Library::Adjectives(Context * context, LibraryOutput & output)
{
    if (output.Result("Adjectives")) return;

    XalanNavigator rb(context->Manager,context->Document);

    rb.Select("//jj");
    unsigned count = rb.getLength();

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"Adjectives",count);
}

void Library::Events(Context * context, LibraryOutput & output)
{
    if (output.Result("Events")) return;
    Amplify::Module::List* eventsList = List::Create("var/module/list/EventsList.txt");

    Amplify::Module::List* eventsListMulti = List::Create("var/module/list/EventsListMulti.txt");
    std::string tmpfilename = Resource::QualifyPath("var/module/list/EventsListMulti.txt");
    std::set<std::string>listSet = eventsListMulti->getListSet(tmpfilename);


    XalanNavigator et(context->Manager,context->Document);

    unsigned count=0;
	
    et.Select("//text()");
    for (unsigned eti=0;eti<et.getLength();++eti)
	{
            XalanNode * node = et.item(eti);
            if (eventsList->Contains(node))
                ++count;
	}
	
    count += static_cast<unsigned>(eventsListMulti->MutliWordCount(listSet,context->Text));
	
    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"Events",count);
}

void Library::TemporalPhrases(Context * context, LibraryOutput & output)
{
    if (output.Result("TemporalPhrases")) return;
    XalanNavigator et(context->Manager,context->Document);
    et.Select("//tp");
    unsigned count = et.getLength();
	
    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"TemporalPhrases",count);
}

void Library::FirstOrSecondPersonPronounAsSubject(Context * context, LibraryOutput & output)
{
    if (output.Result("FirstOrSecondPersonPronounAsSubject")) return;
    XalanNavigator et(context->Manager,context->Document);
    et.Select("//text()[.='i' or .='I' or .='We' or .='WE' or .='we' or .='you' or .='YOU' or .='You']");
    unsigned count = et.getLength();
    /*for(int i=0;i<et.getLength();i++)
      {
      std::string value;
      XalanNode * node = et.item(i);
      XalanLibrary::ToString(node->getNodeValue(),value);
      Lowercase(value);
      if( (value == "i") ||(value == "we") ||(value == "you"))
      count ++;
      }*/
	
    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"FirstOrSecondPersonPronounAsSubject",count);
}
void Library::FutureReference(Context * context, LibraryOutput & output)
{
	
    if (output.Result("FutureReference")) return;
    unsigned count=0;
    XalanNavigator et(context->Manager,context->Document);
    XalanNavigator fr(context->Manager,context->Document);
    /**a) tag = <vmi> containing a child with tag = “VB PRS MOD” whose lemma = “will” | “shall” and a child with
       tag = “VB INF” (the VB INF does not have to be immediately following the "will"/"shall" 
       as long as it is within the same <vmi>, there can be other children in the same <vmi>) (Ex. 1, 2)
    */
    et.Select("//vmi/vb[@msd='prs mod' and @base[.='Will' or .='will' or .='WILL' or .='SHALL' or .='Shall' or .='shall']]");///following::node()[name()='vb'][@msd='inf']");
    for(unsigned i=0;i<et.getLength();i++)
	{
            if(fr.Evaluate(et.item(i),"./following::node()[name()='vb'][@msd='inf']","./ancestor::vmi"))
                count++;
	}
    et.Select("//vb[@msd [.='prs sy3' or .='prs sy1' or .='prs sn3']and @base='be']//following::node()[@base='go']//following::node()[1][@base='to']//following::node()[1][name() ='vb'][@msd='inf']");
    count += et.getLength();
    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"FutureReference",count);
}

void Library::YoungCount(Context * context, LibraryOutput & output)
{
    if (output.Result("YoungCount")) return;

    // Age 4.1.1: 
    // YoungCount = number of words in the text whose lemmas are in YoungList 
			
    // Young List
    Amplify::Module::List* lstYoung = List::Create("var/module/list/YoungList.txt");

    XalanNavigator yc(context->Manager,context->Document);

    int youngCount=0;

    yc.Select("//text()/parent::node()/@base");
    for (unsigned yci=0;yci<yc.getLength();++yci)
	{
            if (lstYoung->Contains(yc.item(yci)))
                ++youngCount;
	}
	
    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"YoungCount",youngCount);
}

void Library::YoungBigramCount(Context * context, LibraryOutput & output)
{
    if (output.Result("YoungBigramCount")) return;

    // Age 4.1.1: 
    //YoungBigramCount = number of bigrams in the text whose lemmas are in YoungBigramList 
			
    // Young Bigram List
    Amplify::Module::MultiwordList* lstYoungBigram = MultiwordList::Create("var/module/list/YoungBigramList.txt");

    std::string tmpStr;
    std::string prevStr;
    int youngBigramCount = 0;
    std::vector<std::string> tmpPhrase; 
    std::vector<std::pair <XalanNode *, std::string> >::iterator lemmaIter;

    for (lemmaIter=context->getLemmas()->begin(); lemmaIter != context->getLemmas()->end(); ++lemmaIter)
	{
            prevStr = tmpStr;
            tmpStr = lemmaIter->second;
            tmpPhrase.clear();
            tmpPhrase.push_back(prevStr);
            tmpPhrase.push_back(tmpStr);
            if (lstYoungBigram->FindPhrase(&tmpPhrase))
                youngBigramCount++;				
	}
	
    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"YoungBigramCount",youngBigramCount);
}

void Library::YoungRatio(Context * context, LibraryOutput & output)
{
    if (output.Result("YoungRatio")) return;

    //(YoungCount+YoungBigramCount) / NumberOfWords 
	
    double youngRatio = 0;
    int tokensCount = 0, youngCount = 0, youngBigramCount = 0;
	
    Execute(context,output,"YoungCount",youngCount);
    Execute(context,output,"YoungBigramCount",youngBigramCount);
    Execute(context,output,"NumberOfWords",tokensCount);	

    if(tokensCount == 0)
        youngRatio = 0;
    else
        youngRatio = (double)(youngCount+youngBigramCount) / (double)tokensCount;	

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"YoungRatio",youngRatio);
}

void Library::SlangRatio(Context * context, LibraryOutput & output)
{
    if (output.Result("SlangRatio")) return;

    // Calculated in PV-Slang 4.1: 

    //Return the ratio (SlangWords / NumberOfWords) * 100

    int tokCount = 0, slangCount = 0;
    double slangRatio = 0;

    Execute(context,output,"SlangWords",slangCount);
    Execute(context,output,"NumberOfWords",tokCount);
    if(tokCount == 0)
        slangRatio = 0;
    else
        slangRatio = ((double)slangCount / (double)tokCount) * 100;
	
    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"SlangRatio",slangRatio);
}

void Library::SeniorCount(Context * context, LibraryOutput & output)
{
    if (output.Result("SeniorCount")) return;

    Amplify::Module::List* lstSenior = List::Create("var/module/list/SeniorList.txt");

    XalanNavigator sc(context->Manager,context->Document);

    int seniorCount=0;

    sc.Select("//text()/parent::node()/@base");
    for (unsigned sci=0;sci<sc.getLength();++sci)
	{
            if (lstSenior->Contains(sc.item(sci)))
                ++seniorCount;
	}
	
    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"SeniorCount",seniorCount);
}

void Library::SeniorBigramCount(Context * context, LibraryOutput & output)
{
    if (output.Result("SeniorBigramCount")) return;

    Amplify::Module::MultiwordList* lstSeniorBigram = MultiwordList::Create("var/module/list/SeniorBigramList.txt");

    std::string tmpStr;
    std::string prevStr;
    int seniorBigramCount = 0;
    std::vector<std::string> tmpPhrase; 
    std::vector<std::pair <XalanNode *, std::string> >::iterator lemmaIter;

    for (lemmaIter=context->getLemmas()->begin();lemmaIter != context->getLemmas()->end(); ++lemmaIter)
	{
            prevStr = tmpStr;
            tmpStr = lemmaIter->second;
            tmpPhrase.clear();
            tmpPhrase.push_back(prevStr);
            tmpPhrase.push_back(tmpStr);
            if (lstSeniorBigram->FindPhrase(&tmpPhrase))
                seniorBigramCount++;				
	}
	
    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"SeniorBigramCount",seniorBigramCount);
}

void Library::SeniorRatio(Context * context, LibraryOutput & output)
{
    if (output.Result("SeniorRatio")) return;

    double seniorRatio = 0;
    int tokensCount = 0, seniorCount = 0, seniorBigramCount = 0;
	
    Execute(context,output,"SeniorCount",seniorCount);
    Execute(context,output,"SeniorBigramCount",seniorBigramCount);
    Execute(context,output,"NumberOfWords",tokensCount);	
    if(tokensCount == 0)
        seniorRatio = 0;
    else
        seniorRatio = (double)(seniorCount+seniorBigramCount) / (double)tokensCount;	


    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"SeniorRatio",seniorRatio);
}


void Library::FemaleRatio(Context * context, LibraryOutput & output)
{
    if (output.Result("FemaleRatio")) return;

    int femaleCount = 0, maleCount = 0;
    double femaleRatio = 0;

    Execute(context,output,"FemaleWords",femaleCount);
    Execute(context,output,"MaleWords",maleCount);
    int sum  =femaleCount + maleCount;
    if(sum == 0)
        femaleRatio = 0;
    else
        femaleRatio = (double)femaleCount / (double)(femaleCount + maleCount);
	
    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"FemaleRatio",femaleRatio);
}

void Library::MaleRatio(Context * context, LibraryOutput & output)
{
    if (output.Result("MaleRatio")) return;

    int femaleCount = 0, maleCount = 0;
    double maleRatio = 0;

    Execute(context,output,"FemaleWords",femaleCount);
    Execute(context,output,"MaleWords",maleCount);
    int sum  =femaleCount + maleCount;
    if(sum == 0)
        maleRatio = 0;
    else
        maleRatio =(double)maleCount / (double)(femaleCount + maleCount);
	
    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"MaleRatio",maleRatio);
}

void Library::RatioNpmodToNp(Context * context, LibraryOutput & output)
{
    if (output.Result("RatioNpmodToNp")) return;

    int numberOfNPmod = 0, numberOfNPs = 0;
    double ratioNpmodToNp = 0;
	
    Execute(context,output,"NumberOfNPmod",numberOfNPmod);
    Execute(context,output,"NPs",numberOfNPs);
    if(numberOfNPs == 0)
        ratioNpmodToNp = 0;
    else
        ratioNpmodToNp = (double)numberOfNPmod / (double)numberOfNPs;

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"RatioNpmodToNp",ratioNpmodToNp);
}


void Library::CommonWordsRatioNew(Context * context, LibraryOutput & output)
{
    if (output.Result("CommonWordsRatioNew")) return;

    int numberOfCommonWords = 0, numberOfWords = 0;
    double ratioCommonWords = 0;
	
    Execute(context,output,"CommonWords",numberOfCommonWords);
    Execute(context,output,"NumberOfWords",numberOfWords);

    if(numberOfWords == 0)
        ratioCommonWords = 0;
    else
        ratioCommonWords =(double)numberOfCommonWords / (double)numberOfWords;

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"CommonWordsRatioNew",ratioCommonWords);
}

void Library::FleschKincaidGradeLevel(Context * context, LibraryOutput & output)
{
    if (output.Result("FleschKincaidGradeLevel")) return;

    double meanSentenceLength = 0, meanSyllables = 0, meanSyntacticDepth = 0;
	
    Execute(context,output,"MeanSentenceLength",meanSentenceLength);
    Execute(context,output,"MeanSyllables",meanSyllables);
    Execute(context,output,"MeanSyntacticDepth",meanSyntacticDepth);

    double fleschKincaidGradeLevel = (.39 * meanSentenceLength) + (11.8 * meanSyllables) - 25.59 + (10 * meanSyntacticDepth); 

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"FleschKincaidGradeLevel",fleschKincaidGradeLevel);
}

void Library::PercentageFlamboyantModifiers(Context * context, LibraryOutput & output)
{
    if (output.Result("PercentageFlamboyantModifiers")) return;

    int flamboyanceWords = 0, modifierWords = 0;
    double percentageFlamboyantModifiers = 0;
	
    Execute(context,output,"FlamboyanceWords",flamboyanceWords);
    Execute(context,output,"ModifierWords",modifierWords);

    if(modifierWords == 0)
        percentageFlamboyantModifiers = 0;
    else
        percentageFlamboyantModifiers = ((double)flamboyanceWords / (double)modifierWords) * 100;

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"PercentageFlamboyantModifiers",percentageFlamboyantModifiers);
}

void Library::PercentageLongAdjectivesAndAdverbs(Context * context, LibraryOutput & output)
{
    if (output.Result("PercentageLongAdjectivesAndAdverbs")) return;

    int longAdjectives = 0,  longAdverbs = 0, adjectives = 0, adverbs = 0;
    double percentageLongAdjectivesAndAdverbs = 0;
	
    Execute(context,output,"LongAdjectives",longAdjectives);
    Execute(context,output,"LongAdverbs",longAdverbs);
    Execute(context,output,"Adjectives",adjectives);
    Execute(context,output,"Adverbs",adverbs);
    int sum = adjectives + adverbs;
    if(sum == 0)
        percentageLongAdjectivesAndAdverbs = 0;
    else
        percentageLongAdjectivesAndAdverbs = ((double)(longAdjectives + longAdverbs)/(double)(adjectives + adverbs)) * 100; 

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"PercentageLongAdjectivesAndAdverbs",percentageLongAdjectivesAndAdverbs);
}

void Library::NumberOfSyllables(Context * context, LibraryOutput & output)
{
    if (output.Result("NumberOfSyllables")) return;

    XalanNavigator ns(context->Manager,context->Document);
    Amplify::Feature::Education education;

    int numberOfWords = 0;
    int numberOfSyllables = 0;
    std::string word;

    ns.Select("//text()");
    for (unsigned nsi=0;nsi<ns.getLength();++nsi)
	{
            XalanNode *node = ns.item(nsi);
            word.clear();
            XalanLibrary::Print(node, word);
            education.characters(word,numberOfWords,numberOfSyllables);		
	}
    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"NumberOfSyllables",numberOfSyllables);	
}



void Library::HumanOrCollectiveAgents(Context * context, LibraryOutput & output)
{
    if (output.Result("HumanOrCollectiveAgents")) return;

    XalanNavigator et(context->Manager,context->Document);
	
    //<nps>’s consisting of one subject personal pronoun, allowed Pos tags: “PP SN3 SUB”, “PP SY3 SUB” (Ex. 1) /
    et.Select("//nps/pp/@msd[ .='sy3 sub' or .='sn3 sub']");
    unsigned count = et.getLength();
    //The head (the last word) of the <nps> is tagged “PM.*NOM” (PM PLU NOM or PM SIN NOM) (Ex. 2) 
    et.Select("//nps//child::node()[last()][name()='pm']/@msd[.='sin nom' or .='plu nom']");
    count += et.getLength();

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"HumanOrCollectiveAgents",count);
}
void Library::LongAdjectives(Context * context, LibraryOutput & output)
{
    if (output.Result("LongAdjectives")) return;

    XalanNavigator rb(context->Manager,context->Document);

    rb.Select("//jj/text()[string-length(.) > 7]");
    unsigned count = rb.getLength();
	

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"LongAdjectives",count);
}
void Library::LongAdverbs(Context * context, LibraryOutput & output)
{
    if (output.Result("LongAdverbs")) return;

    XalanNavigator rb(context->Manager,context->Document);

    rb.Select("//rb/text()[string-length(.) > 9]");
    unsigned count = rb.getLength();
	

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"LongAdverbs",count);
}
void Library::ModifiersWithinNPs(Context * context, LibraryOutput & output)
{
    if (output.Result("ModifiersWithinNPs")) return;

    XalanNavigator rb(context->Manager,context->Document);

    rb.Select("//node()[name()='np' or name()='npo' or name()='nps' or name()='npx' ]//child::node()[name()='jj' or name()='rb' or name()='ql' or (name()='vb' and @msd='pcn')]");
    unsigned count = rb.getLength();
	

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"ModifiersWithinNPs",count);
}
void Library::Negation(Context * context, LibraryOutput & output)
{
    if (output.Result("Negation")) return;

    XalanNavigator rb(context->Manager,context->Document);

    rb.Select("//rb[@msd='neg']");
    unsigned count = rb.getLength();
	

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"Negation",count);
}

void Library::NumberOfClauses(Context * context, LibraryOutput & output)
{
    if (output.Result("NumberOfClauses")) return;

    XalanNavigator rb(context->Manager,context->Document);

    rb.Select("//cl");
    unsigned count = rb.getLength();
	

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"NumberOfClauses",count);
}
void Library::NumberOfNPmod(Context * context, LibraryOutput & output)
{
    if (output.Result("NumberOfNPmod")) return;

    XalanNavigator rb(context->Manager,context->Document);
    XalanNavigator inner(context->Manager,context->Document);

    unsigned count=0;
	
    rb.Select("//node()[name()='np' or name()='npo' or name()='nps' or name()='npx']");
    for(unsigned i=0;i<rb.getLength();i++)
	{
            inner.Select(rb.item(i),"./child::node()[name()='jj' or name()='rb' or name()='ql' or (name()='vb' and @msd='pcn')]");
            if( inner.getLength())
                count++;

	}

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"NumberOfNPmod",count);
}
void Library::NumberOfSentences(Context * context, LibraryOutput & output)
{
    if (output.Result("NumberOfSentences")) return;

    XalanNavigator rb(context->Manager,context->Document);

    rb.Select("//s");
    unsigned count = rb.getLength();
	

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"NumberOfSentences",count);
}

void Library::ObjectNPs(Context * context, LibraryOutput & output)
{
    if (output.Result("ObjectNPs")) return;

    XalanNavigator rb(context->Manager,context->Document);

    rb.Select("//npo");
    unsigned count = rb.getLength();
	

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"ObjectNPs",count);
}
void Library::OfferingGuidanceNecessityModals(Context * context, LibraryOutput & output)
{
    if (output.Result("OfferingGuidanceNecessityModals")) return;

    XalanNavigator rb(context->Manager,context->Document);

    //Rule: tok = "you" + next token = "should" | "ought" | "must"
    std::string xysom = ".//child::text()[.='you' or .='You' or .='YOU']/following::text()[1][.='should' or .='Should' or .='SHOULD' or .='ought' or .='Ought'  or .='OUGHT' or .='must' or .='Must' or .='MUST']";
    rb.Select(xysom.c_str());
    unsigned count = rb.getLength();

    // you + next token has tag = "rb pos"+ next token = should/ought/must
    std::string xyrbsom = ".//child::text()[.='you' or .='You' or .='YOU']/following::text()[1]/parent::rb[@msd='pos']/following::text()[1][.='should' or .='Should' or .='SHOULD' or .='ought' or .='Ought'  or .='OUGHT' or .='must' or .='Must' or .='MUST']";
    rb.Select(xyrbsom.c_str());
    count += rb.getLength();

    // you + next token = have/need + next token = 'to'
    std::string xyhnto = ".//child::text()[.='you' or .='You' or .='YOU']/following::text()[1][.='have' or .='Have' or .='HAVE' or .='need' or .='NEED' or .='Need']/following::text()[1][.='to' or .='To' or .='TO']";
    rb.Select(xyhnto.c_str());
    count += rb.getLength();

    // you + next token has tag = 'rb pos' + next token = have/need + next token = 'to'
    std::string xyrbhnto = ".//child::text()[.='you' or .='You' or .='YOU']/following::text()[1]/parent::rb[@msd='pos']/following::text()[1][.='have' or .='Have' or .='HAVE' or .='need' or .='NEED' or .='Need']/following::text()[1][.='to' or .='To' or .='TO']";
    rb.Select(xyrbhnto.c_str());
    count += rb.getLength();

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"OfferingGuidanceNecessityModals",count);
}
void Library::OfferingGuidancePredictionAndPossibilityModals(Context * context, LibraryOutput & output)
{
    if (output.Result("OfferingGuidancePredictionAndPossibilityModals")) return;

    XalanNavigator rb(context->Manager,context->Document);

    //Rule: tok = "you" + next token = "can" | "could" | "may" | "might" | "will" | "would" | "shall" 
    std::string xyccmmwws = ".//child::text()[.='you' or .='You' or .='YOU']/following::text()[1][.='can' or .='CAN' or .='Can' or .='could' or .='Could' or .='COULD' or .='May' or .='MAY' or .='may' or .='might' or .='Might' or .='MIGHT' or .='will' or .='WILL' or .='Will' or .='would' or .='WOULD' or .='Would' or .='shall' or .='Shall' or .='SHALL']";
    rb.Select(xyccmmwws.c_str());
    unsigned count = rb.getLength();

    // you + next token <rb @msd='pos' ... > + next token = can/could/may/might/will/would/shall
    std::string xyrbccmmwws = ".//child::text()[.='you' or .='You' or .='YOU']/following::text()[1]/parent::rb[@msd='pos']/following::text()[1][.='can' or .='CAN' or .='Can' or .='could' or .='Could' or .='COULD' or  .='May' or .='MAY' or .='may' or  .='might' or .='Might' or .='MIGHT' or .='will' or .='WILL' or .='Will' or .='would' or .='WOULD' or .='Would' or .='shall' or .='Shall' or .='SHALL']";
    rb.Select(xyrbccmmwws.c_str());
    count += rb.getLength();


    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"OfferingGuidancePredictionAndPossibilityModals",count);
}
void Library::Passives(Context * context, LibraryOutput & output)
{
    if (output.Result("Passives")) return;

    int acount=0;
    int bcount=0;
    Execute(context,output,"AgentlessPassives",acount);
    Execute(context,output,"ByPassives",bcount);
    int count = acount+ bcount;
	
    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"Passives",count);
}

void Library::StativeVerbs(Context * context, LibraryOutput & output)
{
    if (output.Result("StativeVerbs")) return;

    Amplify::Module::List* lstStativeVerbs = List::Create("var/module/list/StativeVerbs.txt");

    XalanNavigator st(context->Manager,context->Document);

    unsigned count=0;

    st.Select("//text()/parent::node()/@base");
    for (unsigned sti=0;sti<st.getLength();++sti)
	{
            if (lstStativeVerbs->Contains(st.item(sti)))
                ++count;
	}
	
    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"StativeVerbs",count);
}
void Library::SubjectNPs(Context * context, LibraryOutput & output)
{
    if (output.Result("SubjectNPs")) return;

    XalanNavigator rb(context->Manager,context->Document);

    rb.Select("//nps");
    unsigned count = rb.getLength();
	

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"SubjectNPs",count);
}


void Library::IWeNeedTo(Context * context, LibraryOutput & output)
{
    if (output.Result("IWeNeedTo")) return;
    XalanNavigator et(context->Manager,context->Document);
    //XalanNavigator text(context->Manager,context->Document);
    //XalanNavigator text1(context->Manager,context->Document);


    ////4.2.4(5): lemma = "I"|"we" ++ next word has lemma = "need" | "have" ++ next word has lemma = "to" (Ex. 1) 
    //et.Select(".//child::text()[.='I' or .='we']/following::text()[1][.='need' or .='have']/following::text()[1][.='to']");
    et.Select("//@base[.='i' or .='I' or .='we' or .='WE' or .='We']/parent::node()/following::text()[1]/parent::node()/@base[.='need' or .= 'Need' or .='NEED' or .='have' or .= 'HAVE' or .='Have']/parent::node()/following::text()[1]/parent::node()/@base[.='to' or .='TO' or .='To']");
    unsigned count = et.getLength();

    //4.2.4(6): lemma = "I"|"we" ++ next word has tag = "RB POS" ++ next word has lemma = "need" | "have" ++ next word has lemma = "to" (Ex. 2) 
    et.Select("//@base[.='i' or .='I' or .='we' or .='WE' or .='We']/parent::node()/following::text()/parent::rb[@msd='pos']/following::text()[1]/parent::node()/@base[.='need' or .= 'Need' or .='NEED' or .='have' or .= 'HAVE' or .='Have']/parent::node()/following::text()[1]/parent::node()/@base[.='to' or .='TO' or .='To']");
    count += et.getLength();
		
    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"IWeNeedTo",count);
}

void Library::NegativeWords(Context * context, LibraryOutput & output)
{
    if (output.Result("NegativeWords")) return;

    WordScoreList* wordScoreList = WordScoreList::Create("var/module/list/gbu-score-en.txt");

    unsigned count=0;
    for(unsigned tIndex=0;tIndex<context->getLemmas()->size();tIndex++)
	{
            XalanNode *tNode = context->getLemmas()->at(tIndex).first;
            std::string base = context->getLemmas()->at(tIndex).second;
            std::string name = XalanLibrary::getName(tNode);
            Lowercase(base);
            double value = wordScoreList->getWordScore(base,name);
            if(value < 0)
                count++;
	}

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"NegativeWords",count);	
}
void Library::PositiveWords(Context * context, LibraryOutput & output)
{
    if (output.Result("PositiveWords")) return;

    WordScoreList* wordScoreList = WordScoreList::Create("var/module/list/gbu-score-en.txt");

    unsigned count=0;
    for(unsigned tIndex=0;tIndex<context->getLemmas()->size();tIndex++)
	{
            XalanNode *tNode = context->getLemmas()->at(tIndex).first;
            std::string base = context->getLemmas()->at(tIndex).second;
            std::string name = XalanLibrary::getName(tNode);
            Lowercase(base);
            double value = wordScoreList->getWordScore(base,name);
            if(value > 0)
                count++;
	}
 
    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"PositiveWords",count);	
}
void Library::OntologyCategories(Context * context, LibraryOutput & output)
{
    if (output.Result("OntologyCategories")) return;
    unsigned count=0;
    Amplify::Module::Ontology::Ptr ontology = Amplify::Module::Ontology::getDefaultOntology();
    //Apply the HNP logic here..
    XalanNavigator clNavigator(context->Manager,context->Document);
    clNavigator.Select("//cl");
    for(unsigned nIndex = 0; nIndex < clNavigator.getLength(); nIndex++)
	{
            XalanNode *clNode = clNavigator.item(nIndex);
            NounPhrase nphrase ;
            nphrase.OntologyHNP(context,ontology,clNode,output.OntologyResults);

	}
    std::map<std::string,int > ::iterator it;

    output.Result(static_cast<int>(LibraryOutput::SUCCESS),"OntologyCategories",count);	
}

}}
