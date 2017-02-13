import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.microedition.rms.RecordStore;

public class RMS {
	RecordStore rs = null;

	public RMS() {
		try {
			rs = RecordStore.openRecordStore("saveinfo", true);
		} catch (Exception e) {
		}
	}

	public void save(SaveInfo si) {
		try {
			if (rs.getNumRecords() == 0) {
				byte[] tmp = encode(si);
				rs.addRecord(tmp, 0, tmp.length);
			} else {
				byte[] tmp = encode(si);
				rs.setRecord(1, tmp, 0, tmp.length);
			}
		} catch (Exception e) {
		}
	}

	public SaveInfo load() {

		try {
			if (rs.getNumRecords() != 0) {
				return decode(rs.getRecord(1));
			}

		} catch (Exception e) {
		}
		return null;
	}

	public byte[] encode(SaveInfo si) {
		byte[] result = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(bos);
			dos.writeInt(si.bestscore);
			dos.writeBoolean(si.f);
			dos.writeInt(si.nowscroe);
			dos.writeBoolean(si.bgflag);
			dos.writeInt(si.threenewballs[0]);
			dos.writeInt(si.threenewballs[1]);
			dos.writeInt(si.threenewballs[2]);
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					dos.writeBoolean(si.cells.cells[i][j].exist);
					dos.writeInt(si.cells.cells[i][j].color);
					dos.writeInt(si.cells.cells[i][j].x);
					dos.writeInt(si.cells.cells[i][j].y);
				}
			}
			result = bos.toByteArray();
			dos.close();
			bos.close();
		} catch (Exception e) {
			
		}
		return result;
	}

	public SaveInfo decode(byte[] data) {
		SaveInfo si = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			DataInputStream dis = new DataInputStream(bis);
			si = new SaveInfo();
			si.bestscore = dis.readInt();
			si.f = dis.readBoolean();
			si.nowscroe = dis.readInt();
			si.bgflag = dis.readBoolean();
			si.threenewballs[0] = dis.readInt();
			si.threenewballs[1] = dis.readInt();
			si.threenewballs[2] = dis.readInt();
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					si.cells.cells[i][j].exist = dis.readBoolean();
					si.cells.cells[i][j].color = dis.readInt();
					si.cells.cells[i][j].x = dis.readInt();
					si.cells.cells[i][j].y = dis.readInt();
				}
			}
			dis.close();
			bis.close();
		} catch (Exception e) {
			
		}

		return si;
	}

}
