package pheidip.logic;

import java.util.List;

import pheidip.objects.Donation;

public interface DonationTask
{
  DonationControl getControl(Donation d);
  void clearTask(Donation d);
  boolean isTaskCleared(Donation d);
  List<Donation> refreshTaskList();
  String taskName();
}
