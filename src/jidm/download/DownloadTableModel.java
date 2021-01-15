package jidm.download;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class DownloadTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 4420437663092144118L;

	private String[] names = { "Filename", "Progress", "URL", "Status",
			"Speed", "Start Time", "End Time" };

	private ArrayList<StaticDownload> data = new ArrayList<>();

	public DownloadTableModel() {
	}

	@Override
	public String getColumnName(int column) {
		return names[column];
	}

	@Override
	public int getColumnCount() {
		return names.length;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return data.get(rowIndex).getFileName();
		case 1:
			return data.get(rowIndex).getProcess();
		case 2:
			return data.get(rowIndex).getUrl();
		case 3:
			return data.get(rowIndex).getStatus();
		case 4:
			return data.get(rowIndex).getSpeed();
		case 5:
			return data.get(rowIndex).getStartDate();
		case 6:
			return data.get(rowIndex).getEndDate();
		default:
			return null;
		}
	}

	public void update(StaticDownload staticDowload) {
		fireTableRowsUpdated(data.indexOf(staticDowload),
				data.indexOf(staticDowload));
	}

	public void insertDownload(StaticDownload staticDownload) {
		data.add(staticDownload);
		fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
	}

	public void removeDownload(StaticDownload staticDownload) {
		data.remove(staticDownload);
		if (getRowCount() > 0) {
			fireTableRowsDeleted(0, getRowCount() - 1);
		} else {
			fireTableRowsDeleted(0, 0);
		}
	}

	public StaticDownload getDownload(int rowIndex) {
		return data.get(rowIndex);
	}

}
