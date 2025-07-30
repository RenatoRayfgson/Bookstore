package br.edu.ifba.inf008.interfaces;

import java.util.List;

import br.edu.ifba.inf008.interfaces.models.ReportDetails;

public interface IReportPlugin {

    List<ReportDetails> generateLoanReport();

}
