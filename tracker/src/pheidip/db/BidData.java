package pheidip.db;

import java.math.BigDecimal;
import java.util.List;

import pheidip.objects.Bid;
import pheidip.objects.BidSearchParams;
import pheidip.objects.Challenge;
import pheidip.objects.Choice;
import pheidip.objects.ChoiceOption;

public interface BidData {

	public List<Bid> getAllBids();

	public Choice getChoiceById(int choiceId);

	public void insertChoice(Choice choice);

	public void updateChoice(Choice choice);

	public void deleteChoice(int choiceId);

	public ChoiceOption getChoiceOptionById(int optionId);

	public void insertChoiceOption(ChoiceOption choiceOption);

	public void updateChoiceOption(ChoiceOption option);

	public void deleteChoiceOption(int optionId);

	public Challenge getChallengeById(int challengeId);

	public void insertChallenge(Challenge challenge);

	public void updateChallenge(Challenge challenge);

	public BigDecimal getChoiceOptionTotal(int optionId);

	public BigDecimal getChallengeTotal(int challengeId);

	public void deleteChallenge(int challengeId);

	public List<Bid> searchBids(BidSearchParams params);
}