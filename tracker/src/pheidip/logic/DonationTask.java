package pheidip.logic;

import java.util.List;

import pheidip.objects.Donation;

public interface DonationTask
{
  DonationControl getControl(int donationId);
  void clearTask(int donationId);
  public boolean isTaskCleared(Donation d);
  List<Donation> refreshTaskList();
  String taskName();
}
