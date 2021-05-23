

/**
 * This class represents a law.
 */
class Law {
    private String title;
    private KnessetMember initiator;
    private String party;
    private int publicationYear;
    int socialVal;
    int economyVal;
    int politicalVal;
    private int supportingKM;
   /*----=  Constructors  =-----*/

    /**
     * Creates a new law with the given characteristic.
     * @param lawTitle The title of the law.
     * @param lawInitiator The initiator of the law.
	 * @param lawParty The party of the lawInitiator
     * @param lawYearOfPublication The year the book was published.
     * @param lawSocialValue The comic value of the book.
     * @param lawEconomyValue The dramatic value of the book.
     * @param lawPoliticalValue The educational value of the book.
     */
    Law(String lawTitle, KnessetMember lawInitiator, String lawParty, int lawYearOfPublication, int lawSocialValue, int lawEconomyValue,
        int lawPoliticalValue){
        this.title  = lawTitle;
        this.initiator = lawInitiator;
        this.party = lawParty;
        this.publicationYear = lawYearOfPublication;
        socialVal = lawSocialValue;
        economyVal = lawEconomyValue;
        this.politicalVal = lawPoliticalValue;
        this.supportingKM = 1;
    }

   /*----=  Instance Methods  =-----*/

    /**
     * Returns a string representation of the law, which is a sequence
     * of the title, intiator name and year of publication, separated by
     * commas, inclosed in square brackets. For example, if the law is
     * titled "Fix 128 to Bituah Leumi order", was intiated by Eli Alaluf and published in 2016,
     * this method will return the string:
     * "[Fix 128 to Bituah Leumi order,Eli Alaluf,Kulanu,2016]"
     * @return the String representation of this law.
     */
    String stringRepresentation(){
        return "["+title+","+initiator.stringRepresentation()+","+ this.party +","+this.publicationYear+"]";
    }

    /**
     * Adds another KM to support this law
     */
    void addJoinedKnessetMember(){
        this.supportingKM ++;
    }

    /**
     * remove a single KM support. If only 1 KM supports the law, do nothing.
     */
    void subJoinedKnessetMember(){
        this.supportingKM --;
    }

    /**
     * Returns the number of KMs that are currently supporting this Law (including initiator)
     * @return number of Knesset Members that are currently support this law
     */
    int getCurrentNumberOfKnessetMembers(){
        return this.supportingKM;
    }
    /**
     * @return the Law's social value
     */
    int getSocialValue(){
        return this.socialVal;
    }
    /**
     * @return the Law's political value
     */
    int getPoliticalValue(){
        return this.politicalVal;
    }
    /**
     * @return the Law's economic value
     */
    int getEconomyValue(){
        return this.economyVal;
    }
}