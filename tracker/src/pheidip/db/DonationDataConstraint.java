package pheidip.db;

public enum DonationDataConstraint
{
  DonorEmailUnique("A Donor with that e-mail already exists."),
  DonorEmailLowerCase("Donor e-mails must be lowercase."),
  DonorAliasUnique("A Donor with that alias already exists."),
  DonorAliasLowerCase("Donor aliases must be lowercase."),
  DonorPK("A Donor with that ID already exists."),
  DonationFKDonor("This Donor still has a Donation linked to it."),
  DonationFKDomain("Invalid Donation domain."),
  DonationDomainIdUnique("A Donation with that ID already exists."),
  DonationFKBidState("Invalid Donation bid state."),
  DonationFKReadState("Invalid Donation read state."),
  DonationFKCommentState("Invalid Donation comment state."),
  DonationAmountValid("Donation amount must be non-null and greater than zero."),
  DonationPK("A Donation with that ID already exists."),
  SpeedRunNameUnique("A SpeedRun with that name already exists."),
  SpeedRunNameLowerCase("SpeedRun names must be lowercase."),
  SpeedRunPK("A SpeedRun with that ID already exists."),
  ChoiceFKSpeedRun("This SpeedRun still has a Choice linked to it."),
  ChoiceNameUnique("A Choice with that name already exists."),
  ChoiceNameLowerCase("Choice names must be lowercase."),
  ChoicePK("A Choice with that ID already exists."),
  OptionFKChoice("This Choice still has an Option linked to it."),
  OptionNameUnique("An Option with that name already exists."),
  OptionNameLowerCase("Option names must be lowercase."),
  OptionPK("An Option with that ID already exists."),
  ChallengeFKSpeedRun("This SpeedRun still has a Challenge linked to it."),
  ChallengeNameUnique("A Challenge with that name already exists."),
  ChallengeNameLowerCase("Challenge names must be lowercase."),
  ChallengeAmountValid("Challenge amount must be non-null and non-negative."),
  ChallengePK("An Challenge with that ID already exists."),
  ChoiceBidFKDonation("A Bid is still linked to this Donation."),
  ChoiceBidFKOption("A Bid is still linked to this Option."),
  ChoiceBidAmountValid("Bid amount must be non-null and greater than zero."),
  ChoiceBidPK("A ChoiceBid with that ID already exists."),
  ChallengeBidFKDonation("A Bid is still linked to this Donation."),
  ChallengeBidFKChallenge("A Bid is still linked to this Challenge."),
  ChallengeBidAmountValid("Bid amount must be non-null and greater than zero."),
  ChallengeBidPK("A ChallengeBid with that ID already exists."),
  PrizeNameUnique("A Prize with that name already exists."),
  PrizeNameLowerCase("Prize names must be lowercase."),
  PrizePK("A Prize with that ID already exists."),
  PrizeFKDonor("A Prize is still linked to that Donor.");
  
  private static DonationDataConstraint[] _list = DonationDataConstraint.values();
  
  private String errorMessage;

  DonationDataConstraint(String errorMessage)
  {
    this.errorMessage = errorMessage;
  }
  
  public String getErrorMessage()
  {
    return this.errorMessage;
  }
  
  public static DonationDataConstraint get(int i)
  {
    return _list[i];
  }
  
  public static int size()
  {
    return _list.length;
  }
}
