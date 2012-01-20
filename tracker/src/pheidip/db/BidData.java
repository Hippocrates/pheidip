package pheidip.db;

import java.math.BigDecimal;
import java.util.List;

import pheidip.objects.Bid;
import pheidip.objects.Challenge;
import pheidip.objects.Choice;
import pheidip.objects.ChoiceOption;
import pheidip.objects.SearchParameters;

public interface BidData {

	public List<Bid> getAllBids();

	public Choice getChoiceById(int choiceId);

	public void insertChoice(Choice choice);

	public void updateChoice(Choice choice);

	public void deleteChoice(Choice choice);

	public ChoiceOption getChoiceOptionById(int optionId);

	public void insertChoiceOption(ChoiceOption choiceOption);

	public void updateChoiceOption(ChoiceOption option);

	public void deleteChoiceOption(ChoiceOption option);

	public Challenge getChallengeById(int challengeId);

	public void insertChallenge(Challenge challenge);

	public void updateChallenge(Challenge challenge);

	public BigDecimal getChoiceOptionTotal(int optionId);

	public BigDecimal getChallengeTotal(int challengeId);

	public void deleteChallenge(Challenge challenge);

	public List<Bid> searchBids(SearchParameters<Bid> params);

  public List<Bid> searchBidsRange(SearchParameters<Bid> params, int offset, int size);
}