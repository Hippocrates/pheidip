package pheidip.logic;

import java.util.List;

import pheidip.objects.Donation;

public interface DonationTask
{
  void clearTask(Donation d);
  boolean isTaskCleared(Donation d);
  List<Donation> refreshTaskList();
  String taskName();
}
