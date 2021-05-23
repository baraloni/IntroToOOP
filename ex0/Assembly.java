import java.util.Arrays;

class Assembly{
    static private int maxAssemblyCapacity = 120;
    static private int negativeResponse = -1;

    private KnessetMember[] assemblyMembers = new KnessetMember[maxAssemblyCapacity];
    private int[] supportedLawsPerMember = new int[maxAssemblyCapacity];
    private int assemblyMembersPointer = 0;
    private int maxLawCapacity;
    private int maxSupportedLawsPerKnessetMember;
    private Law[] laws;
    private int[] surveyResults;
    private int lawsPointer = 0;

    /**
     * Creates a new assembly with the given parameters.
     * @param maxLawCapacity The maximal number of laws this assembly can hold.
     * @param maxSupportedLawsPerKnessetMember The maximal number of laws this assembly allows a single KnessetMember to support at the
     * same time.
     */
    Assembly(int maxLawCapacity, int maxSupportedLawsPerKnessetMember){
        this.maxLawCapacity = maxLawCapacity;
        this.laws = new Law[this.maxLawCapacity];
        this.surveyResults = new int[this.maxLawCapacity];
        this.maxSupportedLawsPerKnessetMember = maxSupportedLawsPerKnessetMember;
    }

    /**
     * Adds the given law to this assembly, if there is place available, and it isn't already in the assembly.
     * @param law The law to add to this library.
     * @param surveyResult The survey result of the law.
     * @return a non-negative id number for the law if there was a spot and the law was successfully
     * added, or if the law was already in the assembly; a negative number otherwise.
     */
    int addLawToAssembly(Law law, int surveyResult) {
        if ((this.lawsPointer < this.maxLawCapacity) & (!Arrays.asList(this.laws).contains(law))){
            this.laws[this.lawsPointer] = law;
            this.surveyResults[this.lawsPointer] = surveyResult;
            this.lawsPointer ++;
            return this.lawsPointer - 1;
        }return negativeResponse;
    }

    /**
     * updates the survey result of lawId with a new value
     * @param law law to be updated
     * @param newSurveyValue new survey value.
     */
    void updateSurveyResultOfLaw(Law law, int newSurveyValue) {
        int lawId = this.getLawId(law);
        if (this.isLawIDValid(lawId)) {
            this.surveyResults[lawId] = newSurveyValue;
        }
    }

    /**
     * Returns true if the given number is an id of some law in the assembly, false otherwise.
     * @param lawId The id to check.
     * @return true if the given number is an id of some law in the assembly, false otherwise.
     */
    boolean isLawIDValid(int lawId){
        return (lawId >= 0 && lawId < this.maxLawCapacity && this.laws[lawId] != null);
    }

    /**
     * Returns the non-negative id number of the given law if he is discussed by this assembly, -1 otherwise.
     * @param law The law for which to find the id number.
     * @return a non-negative id number of the given law if he is discussed by this assembly, -1 otherwise.
     */
    int getLawId(Law law){
        for (int index = 0 ; index < this.lawsPointer ; index ++){
            if (this.laws[index] == law){
                return index;
            }
        }return negativeResponse;
    }

    /**
     * Registers the given KnessetMember to this assembly, if there is a spot available.
     * @param KnessetMember The KnessetMember to register to this assembly.
     * @return a non-negative id number for the KnessetMember if there was a spot and the patron was successfully
     * registered, a negative number otherwise.
     */
    int registerKnessetMember(KnessetMember KnessetMember){
        if ((this.assemblyMembersPointer < maxAssemblyCapacity) &
        (!Arrays.asList(this.assemblyMembers).contains(KnessetMember))){
            this.assemblyMembers[this.assemblyMembersPointer] = KnessetMember;
            this.assemblyMembersPointer ++;
            return this.assemblyMembersPointer - 1;
        }return negativeResponse;

    }

    /**
     * Returns true if the given number is an id of a KnessetMember in the assembly, false otherwise.
     * @param KnessetMemberId The id to check.
     * @return  true if the given number is an id of a KnessetMember in the assembly, false otherwise.
     */
    boolean isKnessetMemberIdValid(int KnessetMemberId){
        return (KnessetMemberId >= 0 && KnessetMemberId < maxAssemblyCapacity && this.assemblyMembers[KnessetMemberId] != null);
    }

    /**
     * Returns the non-negative id number of the given KnessetMember if he is registered to this assembly, -1 otherwise.
     * @param KnessetMember The KnessetMember for which to find the id number.
     * @return a non-negative id number of the given KnessetMember if he is registered to this assembly, -1 otherwise.
     */
    int getKnessetMemberId(KnessetMember KnessetMember){
        for (int index = 0 ; index < this.assemblyMembersPointer ; index ++){
            if (this.assemblyMembers[index] == KnessetMember){
                return index;
            }
            }return negativeResponse;
    }

    /**
     * adds KnessetMember to law, if the KnessetMember will support the law according to the survey results.
     * @param lawId The id number of the law to borrow.
     * @param KnessetMemberId The id number of the KnessetMember that will support the law.
     * @param surveyResult The survey result of the law to support.
     * @return true if the KnessetMember was added successfully, false otherwise.
     */
    boolean supportLaw(int lawId, int KnessetMemberId, int surveyResult){
        if ((this.isKnessetMemberIdValid(KnessetMemberId)) && (this.supportedLawsPerMember[KnessetMemberId] <
                this.maxSupportedLawsPerKnessetMember) && (this.isLawIDValid((lawId)))) {
            KnessetMember knessetMember = this.assemblyMembers[KnessetMemberId];
            Law law = this.laws[lawId];
            if(knessetMember.willJoinLaw(law, surveyResult)){
                law.addJoinedKnessetMember();
                this.supportedLawsPerMember[KnessetMemberId] ++;
                return true;
            }
        }return false;
    }

    /**
     * Suggest to the KnessetMember with the given id a law which suits him the best (maximum score of the laws in the assembly).
     * @param KnessetMemberId The id number of the KnessetMember to suggest the law to.
     * @return The best law to match the KnessetMember preferences. Null if there aren't any (case all laws get a zero score).
     * available.
     */
    Law suggestLawToKnessetMember(int KnessetMemberId) {
        Law bestLaw = null;
        double maxLawScore = 0;
        if (this.isKnessetMemberIdValid(KnessetMemberId)) {
            KnessetMember knessetMember = this.assemblyMembers[KnessetMemberId];
            for (int index = 0; index < this.lawsPointer; index++) {
                Law law = this.laws[index];
                int surveyResult = this.surveyResults[index];
                double lawScore = knessetMember.getLawScore(law, surveyResult);
                if (maxLawScore < lawScore){
                    maxLawScore = lawScore;
                    bestLaw = law;
                }
            }
        }return bestLaw;
    }

}