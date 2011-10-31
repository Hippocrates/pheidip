package pheidip.db;

import java.math.BigDecimal;
import java.util.List;

import pheidip.objects.Bid;
import pheidip.objects.Challenge;
import pheidip.objects.Choice;
import pheidip.objects.ChoiceOption;

public interface BidData {

	public List<Bid> getAllBids();

	public Choice getChoiceById(int choiceId);

	public void insertChoice(Choice choice);

	public void updateChoice(Choice choice);

	public List<Choice> getChoicesBySpeedrun(int speedRunId);

	public void deleteChoice(int choiceId);

	public ChoiceOption getChoiceOptionById(int optionId);

	public void insertChoiceOption(ChoiceOption choiceOption);

	public void updateChoiceOption(ChoiceOption option);

	public List<ChoiceOption> getChoiceOptionsByChoiceId(int choiceId);

	public void deleteChoiceOption(int optionId);

	public Challenge getChallengeById(int challengeId);

	public void insertChallenge(Challenge challenge);

	public void updateChallenge(Challenge challenge);

	public List<Challenge> getChallengesBySpeedrun(int speedRunId);

	public BigDecimal getChoiceOptionTotal(int optionId);

	public BigDecimal getChallengeTotal(int challengeId);

	public void deleteChallenge(int challengeId);

	public List<ChoiceOption> getAllChoiceOptions();

}