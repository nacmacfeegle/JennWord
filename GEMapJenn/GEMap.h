#ifndef GEMAP_H
#define GEMAP_H

#include <string>
#include <vector>
#include <climits>

// Initialiser types;
#define GEMAP_TOTINITIALISER 2
enum {GEMAP_RND, GEMAP_SI};
static std::string GEMAP_STRINITIALISER[GEMAP_TOTINITIALISER] = {"RND", "SI"};
static std::string GEMAP_DETAILSINITIALISER[GEMAP_TOTINITIALISER] ={
	"Random init. (remember to set minimum and maximum genome sizes)",
	"Sensible init. (remember to set max depth and tail ratio)"
	};

// Symbol types;
enum {GEMAP_NT, GEMAP_T, GEMAP_DEF, GEMAP_OR, GEMAP_SP, GEMAP_QT, GEMAP_XO, GEMAP_UNDEF};

// Parser states;
enum {GEMAP_RULEDEF, GEMAP_SIGNDEF, GEMAP_PRULE};

// Hash table items;
struct geMapHash{
	std::string symbolName;
	size_t symbolType;
	std::vector<std::vector<std::string> > prods;
	std::vector<int> recProds;
	std::vector<int> depthProds;
	int recRule;
	int minDepth;
	size_t minRepeat;
	size_t maxRepeat;
};

#define GEMAP_PARAMSSTRING		"G:W:N:z:Z:d:D:T:"
#define GEMAP_DEFAULT_GRAMMAR		"grammar.bnf"
#define GEMAP_DEFAULT_MAXWRAP		0
#define GEMAP_DEFAULT_INITIALISER	GEMAP_SI
#define GEMAP_DEFAULT_MINRNDGENOMESIZE	100
#define GEMAP_DEFAULT_MAXRNDGENOMESIZE	100
#define GEMAP_DEFAULT_SIMINDEPTH	0
#define GEMAP_DEFAULT_SIMAXDEPTH	15
#define GEMAP_DEFAULT_SITAILRATIO	0.5

class GEMap{
	public:
		GEMap();
		// Parameter methods;
		static std::string getParamsString();
		static std::string getHelpString();
		static bool paramClash(const std::string&);
		void scanParams(int &, char **);
		void extractParams(int &, char **, bool = true);
		std::string outputParams(const bool = false);
		std::string getGrammarFile() const;
		void setGrammarFile(const std::string&);
		size_t getMaxWraps() const;
		void setMaxWraps(const size_t&);
		size_t getInitialiser() const;
		void setInitialiser(const size_t&);
		std::string getInitialiserStr() const;
		void setInitialiserStr(const std::string&);
		size_t getMinRndGenomeSize() const;
		void setMinRndGenomeSize(const size_t&);
		size_t getMaxRndGenomeSize() const;
		void setMaxRndGenomeSize(const size_t&);
		size_t getSIMinDepth() const;
		void setSIMinDepth(const size_t&);
		size_t getSIMaxDepth() const;
		void setSIMaxDepth(const size_t&);
		double getSITailRatio() const;
		void setSITailRatio(const double&);
		bool readBNFFile(const std::string&, bool = true);
		bool readBNFString(const std::string&, bool = true);
		bool readEBNFFile(const std::string&);
		bool readEBNFString(const std::string&);
		bool mapGE(const std::vector<size_t>&, std::string &, size_t &,
			size_t = 1000000);
		bool mapGE(const std::vector<size_t>&, std::string &,
			size_t &, std::vector<size_t>&,
			size_t = 1000000);
		bool mapGE(const std::vector<size_t>&, std::string &,
			size_t &, std::vector<size_t>&,
			std::vector<std::string>&, size_t = 1000000, bool = true);
		bool mapGE(const std::vector<size_t>&, std::string &,
			size_t &, std::vector<size_t>&, std::vector<size_t>&,
			std::vector<std::string>&, size_t = 1000000, bool = true);
		bool unmapGE(const std::string &, std::vector<size_t>&);
		bool initGE(std::vector<size_t>&, std::string &, size_t &,
			const bool & = false, const size_t & = UINT_MAX);
		bool initGE(std::vector<size_t>&, std::string &, size_t &,
			const double &, const bool & = false, const size_t & = UINT_MAX);
		bool initGE(std::vector<size_t>&, std::string &, size_t &,
			std::vector<size_t> &, const bool & = false, const size_t & = UINT_MAX);
		bool initGE(std::vector<size_t>&, std::string &, size_t &,
			std::vector<size_t> &, const double &, const bool & = false,
			const size_t & = UINT_MAX);
		bool rInitGE(std::vector<size_t>&, std::string &, size_t &,
			std::vector<size_t> &, const double &, const bool & = false,
			const size_t & = UINT_MAX);
		bool sInitGE(std::vector<size_t>&, std::string &, size_t &,
			std::vector<size_t> &, const double &, const bool & = false,
			const size_t & = UINT_MAX);
		// Public attributes;
		std::vector<std::string> tokens;
		std::string startSymbol;
		geMapHash& hDef(const std::string&);
	private:
		// Parameters;
		std::string grammarFile;
		size_t maxWrap;
		size_t initialiser;
		size_t minRndGenomeSize;
		size_t maxRndGenomeSize;
		size_t SIMinDepth;
		size_t SIMaxDepth;
		double SITailRatio;
		bool tokenise(const std::string&, const bool & = true);
		// Hash table methods;
		std::vector<std::vector<geMapHash> > hTable;
		bool getHiAndHo(const std::string&, size_t&, size_t&) const;
		void clearHash(const size_t & = 0);
		void hashAddProduction(const std::string&, const size_t&,
			const std::vector<std::string>&);
		size_t hashFn(const std::string&) const;
		// geMapHash individual used for references not found;
		geMapHash blankFella;
		// SI methods;
		void updateRecAndDepth();
		void setRecAndDepth(std::string, std::vector<std::string>&);
		size_t sInitGEExpand(std::string&, std::vector<size_t>&,
			std::vector<size_t>&, const size_t, const size_t &,
			const bool &, std::string &);
};

// Operator >> : print... grammar?
// Operator << : read grammar?

#endif

